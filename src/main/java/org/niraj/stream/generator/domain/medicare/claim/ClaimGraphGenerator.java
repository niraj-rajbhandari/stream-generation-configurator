package org.niraj.stream.generator.domain.medicare.claim;

import org.niraj.stream.generator.GraphGenerator;
import org.niraj.stream.generator.constants.ClaimGraphConstants;
import org.niraj.stream.generator.domain.medicare.pojo.Claim;
import org.niraj.stream.generator.domain.medicare.pojo.Patient;
import org.niraj.stream.generator.domain.medicare.repository.DiagnosisRepository;
import org.niraj.stream.generator.domain.medicare.repository.PatientRepository;
import org.niraj.stream.generator.domain.medicare.repository.ProcedureRepository;
import org.niraj.stream.generator.enumerator.EdgeType;
import org.niraj.stream.generator.pojo.Edge;
import org.niraj.stream.generator.pojo.StreamConfigurationPattern;
import org.niraj.stream.generator.pojo.Vertex;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimGraphGenerator extends GraphGenerator {

    private Map<String, String> patientsParsed;

    private static ClaimGraphGenerator instance = null;
    private String previousPatient;
    private int visitCount;
    private int currentGraphVertexIndex;
    private int currentGraphEdgeIndex;
    private PatientRepository patientRepo;
    private ProcedureRepository procedureRepo;
    private DiagnosisRepository diagnosisRepo;
    private List<Vertex> vertexList;
    private List<Edge> edgeList;

    private Map<String, Vertex> priorDiagnosisParsed;
    private Map<String, Vertex> actualDiagnosisParsed;
    private Map<String, Vertex> physicianParsed;
    private Map<String, Vertex> procedureParsed;

    private StreamConfigurationPattern streamConfigurationPattern;

    private ClaimGraphGenerator() throws FileNotFoundException {
        this.patientsParsed = new HashMap<>();
        this.resetCounters();
        this.streamConfigurationPattern = new StreamConfigurationPattern();
        this.patientRepo = PatientRepository.getInstance();
        this.diagnosisRepo = DiagnosisRepository.getInstance();
        this.procedureRepo = ProcedureRepository.getInstance();
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
    }

    public static ClaimGraphGenerator getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new ClaimGraphGenerator();
        }
        return instance;
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public void setVertexList(List<Vertex> vertexList) {
        this.vertexList = vertexList;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(List<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    public List<StreamConfigurationPattern> getGraphStream() throws IllegalAccessException {
        if (this.edgeList.isEmpty() || this.vertexList.isEmpty()) {
            throw new IllegalAccessException("Graph is not created to access this method, call createGraphStream() first");
        }
        List<StreamConfigurationPattern> graphStreamPatterns = new ArrayList<>();

        this.streamConfigurationPattern.setId(graphStreamPatterns.size() + 1);
        this.streamConfigurationPattern.setTrack(true);
        this.streamConfigurationPattern.setEdges(this.edgeList);
        this.streamConfigurationPattern.setVertices(this.vertexList);
        this.streamConfigurationPattern.setProbability(1);

        graphStreamPatterns.add(this.streamConfigurationPattern);
        return graphStreamPatterns;
    }

    public int createGraphStream(Claim claim) throws FileNotFoundException {
        String subGraphId;

        Patient patient = new Patient();
        patient.setPatientId(claim.getPatientId());

        if (claim.claimCodeExists() && patientRepo.findAll().contains(patient)) {
            if (previousPatient != null && previousPatient.equals(claim.getPatientId())) {
                subGraphId = this.patientsParsed.get(claim.getPatientId());
                this.visitCount++;
            } else {
                this.visitCount = 1;
                this.previousPatient = claim.getPatientId();
                this.patientsParsed.put(claim.getPatientId(),
                        Integer.toString(this.patientsParsed.size() + 1));
                subGraphId = Integer.toString(this.patientsParsed.size());

                this.resetDiagnosisProcedurePhysicianMetaData();
            }


            Vertex patientVertex = createPatientVertex(subGraphId);
            Vertex claimVertex = createClaimVertex(claim.getId(), subGraphId);
            createEdge(patientVertex, claimVertex, EdgeType.DIRECTED, "files", subGraphId);

            Vertex carrierVertex = createCarrierVertex(subGraphId);
            createEdge(claimVertex, carrierVertex, EdgeType.UNDIRECTED, "type", subGraphId);

            createDiagnosisPhysicianGraph(claim, carrierVertex, patientVertex, subGraphId);
        }

        return patientsParsed.size();
    }

    private void resetCounters() {
        this.visitCount = INITIAL_COUNT;
        this.currentGraphVertexIndex = INITIAL_COUNT;
        this.currentGraphEdgeIndex = INITIAL_COUNT;
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

    private void createPhysicianSubGraph(Claim claim, Vertex carrierVertex, Vertex patientVertex, String graphId) {
        for (Map.Entry<Integer, String> physicianEntry : claim.getPhysicianList().entrySet()) {
            String physician = physicianEntry.getValue();
            boolean physicianExists = this.physicianParsed.containsKey(physician);
            Vertex physicianVertex;
            if (!physicianExists) {
                physicianVertex = createPhysicianVertex(physician, graphId);
            } else {
                physicianVertex = this.physicianParsed.get(physician);
            }

            createEdge(carrierVertex, physicianVertex, EdgeType.DIRECTED, "attending", graphId);
            createEdge(patientVertex, physicianVertex, EdgeType.DIRECTED, "visit-" + visitCount + "-attended-by", graphId);

            Vertex actualDiagnosisVertex =
                    createActualDiagnosisSubGraph(claim, patientVertex, physicianVertex, graphId, physicianEntry.getKey());
            createProcedureSubGraph(claim, patientVertex, physicianVertex, actualDiagnosisVertex, graphId, physicianEntry.getKey());
        }
    }

    private Vertex createActualDiagnosisSubGraph(Claim claim, Vertex patientVertex,
                                                 Vertex physicianVertex, String graphId, Integer physicianIndex) {
        String actualDiagnosis = claim.getLineDiagnosisCodeList().get(physicianIndex);
        boolean actualDiagnosisExists = this.actualDiagnosisParsed.containsKey(actualDiagnosis);

        Vertex actualDiagnosisCodeVertex;
        if (!actualDiagnosisExists) {
            actualDiagnosisCodeVertex = createActualDiagnosisCodeVertex(actualDiagnosis, graphId);
        } else {
            actualDiagnosisCodeVertex = this.actualDiagnosisParsed.get(actualDiagnosis);
        }


        createEdge(patientVertex, actualDiagnosisCodeVertex, EdgeType.DIRECTED, "diagnosed-with", graphId);
        createEdge(physicianVertex, actualDiagnosisCodeVertex, EdgeType.DIRECTED, "make-diagnosis", graphId);

        if (!actualDiagnosisExists) {
            String diagnosis = this.diagnosisRepo.find(actualDiagnosis);
            Vertex actualDiagnosisNameVertex = createDiagnosisNameVertex(diagnosis, graphId);
            createEdge(actualDiagnosisCodeVertex, actualDiagnosisNameVertex, EdgeType.UNDIRECTED, "code", graphId);
        }

        return actualDiagnosisCodeVertex;
    }

    private void createProcedureSubGraph(Claim claim, Vertex patientVertex, Vertex physicianVertex,
                                         Vertex actualDiagnosisVertex, String graphId, Integer physicianIndex) {

        String procedureRecommended = claim.getHCPCSCodeList().get(physicianIndex);
        boolean procedureExists = this.procedureParsed.containsKey(procedureRecommended);

        Vertex procedureVertex;
        String claimStatus = claim.getLineProcessingIndicationCodeList().get(physicianIndex);
        if (!procedureExists) {
            procedureVertex = createProcedureVertex(procedureRecommended, graphId, claimStatus);
            this.procedureParsed.put(procedureRecommended, procedureVertex);
        } else {
            procedureVertex = this.procedureParsed.get(procedureRecommended);
        }

        createEdge(actualDiagnosisVertex, procedureVertex, EdgeType.DIRECTED, "treated-with", graphId);
        createEdge(physicianVertex, procedureVertex, EdgeType.DIRECTED, "on-visit-" + visitCount + "-recommend", graphId);
        createEdge(patientVertex, procedureVertex, EdgeType.DIRECTED, "on-visit-" + visitCount + "-received", graphId);
    }

    private Vertex createPatientVertex(String graphId) {
        return createVertex(this.getVertexAttribute(ClaimGraphConstants.PATIENT_VERTEX_LABEL, graphId));
    }

    private Vertex createClaimVertex(String claimId, String graphId) {

        Map<String, String> claimVertexAttributes =
                this.getVertexAttribute(ClaimGraphConstants.CLAIM_VERTEX_LABEL, graphId);
        claimVertexAttributes.put(COMMENT_ATTRIBUTE_INDEX, "//Carrier Claim ID " + claimId);

        return createVertex(claimVertexAttributes);
    }

    private Vertex createCarrierVertex(String graphId) {

        return createVertex(this.getVertexAttribute(ClaimGraphConstants.CARRIER_VERTEX_LABEL, graphId));
    }

    private Vertex createPriorDiagnosisCodeVertex(String code, String graphId) {


        Map<String, String> priorDiagnosisCodeVertexAttributes =
                this.getVertexAttribute(ClaimGraphConstants.PRIOR_DIAGNOSIS_VERTEX_LABEL, graphId);
        priorDiagnosisCodeVertexAttributes.put(CODE_INDEX, code);

        Vertex priorDiagnosisVertex = createVertex(priorDiagnosisCodeVertexAttributes);
        this.priorDiagnosisParsed.put(code, priorDiagnosisVertex);

        return priorDiagnosisVertex;
    }

    private Vertex createActualDiagnosisCodeVertex(String code, String graphId) {
        Map<String, String> actualDiagnosisCodeVertexAttributes =
                this.getVertexAttribute(ClaimGraphConstants.ACTUAL_DIAGNOSIS_VERTEX_LABEL, graphId);
        actualDiagnosisCodeVertexAttributes.put(CODE_INDEX, code);

        Vertex actualDiagnosisVertex = createVertex(actualDiagnosisCodeVertexAttributes);
        this.actualDiagnosisParsed.put(code, actualDiagnosisVertex);

        return actualDiagnosisVertex;
    }

    private Vertex createDiagnosisNameVertex(String diagnosisName, String graphId) {
        return createVertex(this.getVertexAttribute(diagnosisName, graphId));
    }

    private Vertex createPhysicianVertex(String physicianId, String graphId) {
        Map<String, String> physicianVertexAttributes =
                this.getVertexAttribute(ClaimGraphConstants.PHYSICIAN_VERTEX_LABEL, graphId);
        physicianVertexAttributes.put(COMMENT_ATTRIBUTE_INDEX, "//Provider Physician " + physicianId);
        physicianVertexAttributes.put(ID_INDEX, physicianId);

        Vertex physicianVertex = createVertex(physicianVertexAttributes);
        this.physicianParsed.put(physicianId, physicianVertex);

        return physicianVertex;
    }

    private Vertex createProcedureVertex(String procedureCode, String graphId, String claimStatus) {
        Map<String, String> procedureVertexAttributes =
                this.getVertexAttribute(ClaimGraphConstants.PROCEDURE_VERTEX_LABEL, graphId);
        procedureVertexAttributes.put(CODE_INDEX, procedureCode);

        Vertex procedureVertex = this.createVertex(procedureVertexAttributes);
        this.procedureParsed.put(procedureCode, procedureVertex);

        String procedureCodeName = this.procedureRepo.find(procedureCode);
        Map<String, String> procedureCodeVertexAttributes = this.getVertexAttribute(procedureCodeName, graphId);
        Vertex procedureCodeVertex = this.createVertex(procedureCodeVertexAttributes);
        createEdge(procedureVertex, procedureCodeVertex, EdgeType.UNDIRECTED, "code", graphId);

        Vertex claimStatusVertex = createVertex(getVertexAttribute(claimStatus, graphId));
        createEdge(procedureVertex, claimStatusVertex, EdgeType.UNDIRECTED, "claim-status", graphId);

        return procedureVertex;
    }

    private Vertex createVertex(Map<String, String> attributes) {
        Vertex vertex = new Vertex();
        vertex.setId(Integer.toString(this.currentGraphVertexIndex++));
        vertex.setNew(true);
        vertex.setAttributes(attributes);
        vertexList.add(vertex);
        return vertex;
    }

    private Edge createEdge(Vertex source, Vertex destination, EdgeType edgeType, String label, String graphId) {
        Edge edge = new Edge();
        edge.setId(Integer.toString(this.currentGraphEdgeIndex++));
        edge.setSource(source.getId());
        edge.setTarget(destination.getId());
        edge.setDirected(EdgeType.DIRECTED == edgeType);

        Map<String, String> edgeAttributes = new HashMap<>();
        edgeAttributes.put(LABEL_ATTRIBUTE_INDEX, label);
        edgeAttributes.put(GRAPH_ID_ATTRIBUTE_INDEX, graphId);

        edge.setAttributes(edgeAttributes);

        edgeList.add(edge);

        return edge;
    }

    private Map<String, String> getVertexAttribute(String label, String graphId) {
        Map<String, String> vertexAttributes = new HashMap<>();
        vertexAttributes.put(GRAPH_ID_ATTRIBUTE_INDEX, graphId);
        vertexAttributes.put(LABEL_ATTRIBUTE_INDEX, label);

        return vertexAttributes;
    }
}
