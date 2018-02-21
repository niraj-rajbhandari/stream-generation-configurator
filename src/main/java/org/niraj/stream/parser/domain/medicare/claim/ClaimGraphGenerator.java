package org.niraj.stream.parser.domain.medicare.claim;

import org.niraj.stream.parser.GraphGenerator;
import org.niraj.stream.parser.constants.ClaimGraphConstants;
import org.niraj.stream.parser.domain.medicare.pojo.Claim;
import org.niraj.stream.parser.domain.medicare.pojo.Patient;
import org.niraj.stream.parser.domain.medicare.repository.DiagnosisRepository;
import org.niraj.stream.parser.domain.medicare.repository.PatientRepository;
import org.niraj.stream.parser.domain.medicare.repository.ProcedureRepository;
import org.niraj.stream.parser.enumerator.EdgeType;
import org.niraj.stream.parser.pojo.Vertex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class ClaimGraphGenerator extends GraphGenerator<Claim> {

    private static final Logger LOGGER = Logger.getLogger(ClaimGraphGenerator.class.getName());

    private Map<String, String> patientsParsed;

    private boolean differentXP;

    private Map<String, Vertex> previousPatient;
    private Integer visitCount;

    private PatientRepository patientRepo;
    private ProcedureRepository procedureRepo;
    private DiagnosisRepository diagnosisRepo;


    private Map<String, Vertex> priorDiagnosisParsed;
    private Map<String, Vertex> actualDiagnosisParsed;
    private Map<String, Vertex> physicianParsed;
    private Map<String, Vertex> procedureParsed;


    public ClaimGraphGenerator() throws IOException, TimeoutException {

        super();
        this.patientsParsed = new HashMap<>();


        this.patientRepo = PatientRepository.getInstance();
        this.diagnosisRepo = DiagnosisRepository.getInstance();
        this.procedureRepo = ProcedureRepository.getInstance();

        this.differentXP = new Boolean(this.configReader.getProperty(POSITIVE_GRAPH_PROPERTY));
        this.previousPatient = new HashMap<>();

    }

    public Map<String, String> getPatientsParsed() {
        return this.patientsParsed;
    }


    public void createGraphStream(Claim claim) throws FileNotFoundException {
        String subGraphId;

        Patient patient = new Patient();
        patient.setPatientId(claim.getPatientId());

        Vertex patientVertex;
        if (claim.claimCodeExists() && patientRepo.findAll().contains(patient)) {
            if (!previousPatient.isEmpty() && previousPatient.containsKey(claim.getPatientId())) {
                subGraphId = (this.differentXP) ? this.patientsParsed.get(claim.getPatientId()) : SINGLE_GRAPH;
                this.visitCount++;
                patientVertex = previousPatient.get(claim.getPatientId());
            } else {
                this.resetDiagnosisProcedurePhysicianMetaData();
                this.resetCounters();

                this.patientsParsed.put(claim.getPatientId(),
                        Integer.toString(this.patientsParsed.size() + 1));
                subGraphId = (this.differentXP) ? Integer.toString(this.patientsParsed.size()) : SINGLE_GRAPH;

                patientVertex = createPatientVertex(subGraphId, claim.getPatientId());
                this.previousPatient.put(claim.getPatientId(), patientVertex);
            }

            System.out.println("Graph Id:" + subGraphId);
            Vertex claimVertex = createClaimVertex(claim.getId(), subGraphId);

            createEdge(patientVertex, claimVertex, EdgeType.DIRECTED, "files", subGraphId);

            Vertex carrierVertex = createCarrierVertex(subGraphId);
            createEdge(claimVertex, carrierVertex, EdgeType.UNDIRECTED, "type", subGraphId);

            createDiagnosisPhysicianGraph(claim, carrierVertex, patientVertex, subGraphId);
        }
    }

    private void resetDiagnosisProcedurePhysicianMetaData() {
        this.priorDiagnosisParsed = new HashMap<>();
        this.physicianParsed = new HashMap<>();
        this.procedureParsed = new HashMap<>();
        this.actualDiagnosisParsed = new HashMap<>();
    }

    private void createDiagnosisPhysicianGraph(Claim claim, Vertex carrierVertex, Vertex patientVertex, String graphId)
            throws FileNotFoundException {
        this.createDiagnosisSubGraph(claim, carrierVertex, patientVertex, graphId);
        this.createPhysicianSubGraph(claim, carrierVertex, patientVertex, graphId);
    }

    private void createDiagnosisSubGraph(Claim claim, Vertex carrierVertex, Vertex patientVertex, String graphId)
            throws FileNotFoundException {
        for (Map.Entry<Integer, String> priorDiagnosisEntry : claim.getPriorDiagnosisCodeList().entrySet()) {
            String diagnosisCode = priorDiagnosisEntry.getValue();

            Vertex priorDiagnosisCodeVertex;
            boolean diagnosisExists = this.priorDiagnosisParsed.containsKey(diagnosisCode);
            if (!diagnosisExists) {
                priorDiagnosisCodeVertex = createPriorDiagnosisCodeVertex(diagnosisCode, graphId);
            } else {
                priorDiagnosisCodeVertex = this.priorDiagnosisParsed.get(diagnosisCode);
            }

            createEdge(carrierVertex, priorDiagnosisCodeVertex, EdgeType.DIRECTED, "visit-for", graphId);
            createEdge(patientVertex, priorDiagnosisCodeVertex, EdgeType.DIRECTED, "visit-for", graphId);

            if (!diagnosisExists) {
                String diagnosis = this.diagnosisRepo.find(diagnosisCode);
                Vertex priorDiagnosisNameVertex = createDiagnosisNameVertex(diagnosis, graphId);
                createEdge(priorDiagnosisCodeVertex, priorDiagnosisNameVertex, EdgeType.UNDIRECTED, "code", graphId);
            }
        }
    }

    private void createPhysicianSubGraph(Claim claim, Vertex carrierVertex, Vertex patientVertex, String graphId) throws FileNotFoundException {
        for (Map.Entry<Integer, String> physicianEntry : claim.getPhysicianList().entrySet()) {
            String physician = physicianEntry.getValue();
            boolean newPhysician = !this.physicianParsed.containsKey(physician);
            Vertex physicianVertex;
            if (newPhysician) {
                physicianVertex = createPhysicianVertex(physician, graphId);
            } else {
                physicianVertex = this.physicianParsed.get(physician);
            }

            if (newPhysician) {
                createEdge(carrierVertex, physicianVertex, EdgeType.DIRECTED, "attending", graphId);
                createEdge(patientVertex, physicianVertex, EdgeType.DIRECTED, "visit-" + visitCount + "-attended-by", graphId);
            }

            Vertex actualDiagnosisVertex =
                    createActualDiagnosisSubGraph(claim, patientVertex, physicianVertex, graphId,
                            physicianEntry.getKey(), newPhysician);
            createProcedureSubGraph(claim, patientVertex, physicianVertex, actualDiagnosisVertex, graphId,
                    physicianEntry.getKey(), newPhysician);
        }
    }

    private Vertex createActualDiagnosisSubGraph(Claim claim, Vertex patientVertex,
                                                 Vertex physicianVertex, String graphId,
                                                 Integer physicianIndex, boolean newPhysician) {
        String actualDiagnosis = claim.getLineDiagnosisCodeList().get(physicianIndex);
        boolean newActualDiagnosis = !this.actualDiagnosisParsed.containsKey(actualDiagnosis);

        Vertex actualDiagnosisCodeVertex;
        if (newActualDiagnosis) {
            actualDiagnosisCodeVertex = createActualDiagnosisCodeVertex(actualDiagnosis, graphId);
        } else {
            actualDiagnosisCodeVertex = this.actualDiagnosisParsed.get(actualDiagnosis);
        }

        if (newPhysician || newActualDiagnosis) {
            createEdge(patientVertex, actualDiagnosisCodeVertex, EdgeType.DIRECTED, "diagnosed-with", graphId);
            createEdge(physicianVertex, actualDiagnosisCodeVertex, EdgeType.DIRECTED, "make-diagnosis", graphId);
        }


        if (newActualDiagnosis) {
            String diagnosis = this.diagnosisRepo.find(actualDiagnosis);
            Vertex actualDiagnosisNameVertex = createDiagnosisNameVertex(diagnosis, graphId);
            createEdge(actualDiagnosisCodeVertex, actualDiagnosisNameVertex, EdgeType.UNDIRECTED, "code", graphId);
        }

        return actualDiagnosisCodeVertex;
    }

    private void createProcedureSubGraph(Claim claim, Vertex patientVertex, Vertex physicianVertex,
                                         Vertex actualDiagnosisVertex, String graphId
            , Integer physicianIndex, boolean newPhysician) {

        String procedureRecommended = claim.getHCPCSCodeList().get(physicianIndex);
        boolean newProcedure = !this.procedureParsed.containsKey(procedureRecommended);

        Vertex procedureVertex;
        String claimStatus = claim.getLineProcessingIndicationCodeList().get(physicianIndex);
        if (newProcedure) {
            procedureVertex = createProcedureVertex(procedureRecommended, graphId, claimStatus);
            this.procedureParsed.put(procedureRecommended, procedureVertex);
        } else {
            procedureVertex = this.procedureParsed.get(procedureRecommended);
        }

        createEdge(actualDiagnosisVertex, procedureVertex, EdgeType.DIRECTED, "treated-with", graphId);
        if (newPhysician || newProcedure) {
            createEdge(physicianVertex, procedureVertex, EdgeType.DIRECTED, "on-visit-" + visitCount + "-recommend", graphId);
            createEdge(patientVertex, procedureVertex, EdgeType.DIRECTED, "on-visit-" + visitCount + "-received", graphId);
        }
    }

    private Vertex createPatientVertex(String graphId, String patientId) {
        Map<String, String> attributes = this.getAttribute(ClaimGraphConstants.PATIENT_VERTEX_LABEL, graphId);
        attributes.put(COMMENT_ATTRIBUTE_INDEX, "//Patient Id " + patientId);
        return createVertex(attributes);
    }

    private Vertex createClaimVertex(String claimId, String graphId) {

        Map<String, String> claimVertexAttributes =
                this.getAttribute(ClaimGraphConstants.CLAIM_VERTEX_LABEL, graphId);
        claimVertexAttributes.put(COMMENT_ATTRIBUTE_INDEX, "//Carrier Claim ID " + claimId);

        return createVertex(claimVertexAttributes);
    }

    private Vertex createCarrierVertex(String graphId) {

        return createVertex(this.getAttribute(ClaimGraphConstants.CARRIER_VERTEX_LABEL, graphId));
    }

    private Vertex createPriorDiagnosisCodeVertex(String code, String graphId) {


        Map<String, String> priorDiagnosisCodeVertexAttributes =
                this.getAttribute(ClaimGraphConstants.PRIOR_DIAGNOSIS_VERTEX_LABEL, graphId);
        priorDiagnosisCodeVertexAttributes.put(COMMENT_ATTRIBUTE_INDEX, "//" + CODE_INDEX + " " + code);

        Vertex priorDiagnosisVertex = createVertex(priorDiagnosisCodeVertexAttributes);
        this.priorDiagnosisParsed.put(code, priorDiagnosisVertex);

        return priorDiagnosisVertex;
    }

    private Vertex createActualDiagnosisCodeVertex(String code, String graphId) {
        Map<String, String> actualDiagnosisCodeVertexAttributes =
                this.getAttribute(ClaimGraphConstants.ACTUAL_DIAGNOSIS_VERTEX_LABEL, graphId);
        actualDiagnosisCodeVertexAttributes.put(COMMENT_ATTRIBUTE_INDEX, "//" + CODE_INDEX + " " + code);

        Vertex actualDiagnosisVertex = createVertex(actualDiagnosisCodeVertexAttributes);
        this.actualDiagnosisParsed.put(code, actualDiagnosisVertex);

        return actualDiagnosisVertex;
    }

    private Vertex createDiagnosisNameVertex(String diagnosisName, String graphId) {
        return createVertex(this.getAttribute(diagnosisName, graphId));
    }

    private Vertex createPhysicianVertex(String physicianId, String graphId) {
        Map<String, String> physicianVertexAttributes =
                this.getAttribute(ClaimGraphConstants.PHYSICIAN_VERTEX_LABEL, graphId);
        physicianVertexAttributes.put(COMMENT_ATTRIBUTE_INDEX, "//Provider Physician " + physicianId);
        physicianVertexAttributes.put(ID_INDEX, physicianId);

        Vertex physicianVertex = createVertex(physicianVertexAttributes);
        this.physicianParsed.put(physicianId, physicianVertex);

        return physicianVertex;
    }

    private Vertex createProcedureVertex(String procedureCode, String graphId, String claimStatus) {
        Map<String, String> procedureVertexAttributes =
                this.getAttribute(ClaimGraphConstants.PROCEDURE_VERTEX_LABEL, graphId);
        procedureVertexAttributes.put(CODE_INDEX, procedureCode);

        Vertex procedureVertex = this.createVertex(procedureVertexAttributes);
        this.procedureParsed.put(procedureCode, procedureVertex);

        String procedureCodeName = this.procedureRepo.find(procedureCode);
        Map<String, String> procedureCodeVertexAttributes = this.getAttribute(procedureCodeName, graphId);
        Vertex procedureCodeVertex = this.createVertex(procedureCodeVertexAttributes);
        createEdge(procedureVertex, procedureCodeVertex, EdgeType.UNDIRECTED, "code", graphId);

        Vertex claimStatusVertex = createVertex(getAttribute(claimStatus, graphId));
        createEdge(procedureVertex, claimStatusVertex, EdgeType.UNDIRECTED, "claim-status", graphId);

        return procedureVertex;
    }
}
