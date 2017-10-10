package org.niraj.stream.generator.domain.medicare.claim;

import org.niraj.stream.generator.GraphGenerator;
import org.niraj.stream.generator.domain.medicare.pojo.Claim;
import org.niraj.stream.generator.domain.medicare.pojo.Patient;
import org.niraj.stream.generator.domain.medicare.repository.PatientRepository;
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
    private static final int USER_ID_INDEX = 0;
    private static final int USER_DOC_INDEX = 1;
    private static final int USER_PROCEDURE_INDEX = 2;
    private static final String PATIENT_VERTEX_LABEL = "Patient";
    private static final String CLAIM_VERTEX_LABEL = "Claim";


    private Map<String, List<String>> patientsParsed;

    private static ClaimGraphGenerator instance = null;
    private String previousPatient;
    private int visitCount;
    private int currentGraphVertexIndex;
    private int currentGraphEdgeIndex;
    private PatientRepository patientRepo;

    StreamConfigurationPattern streamConfigurationPattern;

    private ClaimGraphGenerator() throws FileNotFoundException {
        this.patientsParsed = new HashMap<>();
        this.resetCounters();
        this.streamConfigurationPattern = new StreamConfigurationPattern();
        this.patientRepo = PatientRepository.getInstance();
    }

    public static ClaimGraphGenerator getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new ClaimGraphGenerator();
        }
        return instance;
    }

    public void createGraphStream(Claim claim) {
        String subGraphId;
        List<String> userInfo;

        if (previousPatient.equals(claim.getPatientId())) {
            userInfo = this.patientsParsed.get(claim.getPatientId());
            subGraphId = userInfo.get(USER_ID_INDEX);
        } else {
            previousPatient = claim.getPatientId();
            userInfo = new ArrayList<>();
            userInfo.add(Integer.toString((this.patientsParsed.size() + 1)));
            this.patientsParsed.put(claim.getPatientId(), userInfo);
            subGraphId = Integer.toString(this.patientsParsed.size());
        }

        Patient patient = new Patient();
        patient.setPatientId(claim.getPatientId());
        if (claim.claimCodeExists() && patientRepo.findAll().contains(patient)) {
            Vertex patientVertex = getPatientVertex(subGraphId);
            Vertex claimVertex = getClaimVertex(claim.getId(), subGraphId);
        }


    }

    public List<StreamConfigurationPattern> getGraphStream() {
        List<StreamConfigurationPattern> graphStreamPatterns = new ArrayList<>();
        graphStreamPatterns.add(this.streamConfigurationPattern);
        return graphStreamPatterns;
    }

    private void resetCounters() {
        this.visitCount = INITIAL_COUNT;
        this.currentGraphVertexIndex = INITIAL_COUNT;
        this.currentGraphEdgeIndex = INITIAL_COUNT;
    }

    private Vertex getPatientVertex(String graphId) {
        Vertex patientVertex = new Vertex();
        patientVertex.setId(Integer.toString(this.currentGraphVertexIndex++));
        Map<String, String> patientVertexAttributes = new HashMap<>();
        patientVertexAttributes.put(GRAPH_ID_ATTRIBUTE_INDEX, graphId);
        patientVertexAttributes.put(LABEL_ATTRIBUTE_INDEX, PATIENT_VERTEX_LABEL);
        return patientVertex;
    }

    private Vertex getClaimVertex(String claimId, String graphId) {
        Vertex claimVertex = new Vertex();
        claimVertex.setId(Integer.toString(this.currentGraphVertexIndex++));
        Map<String, String> claimVertexAttributes = new HashMap<>();
        claimVertexAttributes.put(GRAPH_ID_ATTRIBUTE_INDEX, graphId);
        claimVertexAttributes.put(LABEL_ATTRIBUTE_INDEX, CLAIM_VERTEX_LABEL);
        claimVertexAttributes.put(COMMENT_ATTRIBUTE_INDEX, "//Carrier Claim ID " + claimId);
        return claimVertex;
    }

    private Edge createEdge(Vertex source, Vertex destination, EdgeType edgeType, String label) {
        Edge edge = new Edge();
        edge.setId(Integer.toString(this.currentGraphEdgeIndex++));
        edge.setSource(source.getId());
        edge.setTarget(destination.getId());

    }
}
