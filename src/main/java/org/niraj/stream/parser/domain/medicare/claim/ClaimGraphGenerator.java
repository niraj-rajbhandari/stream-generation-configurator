package org.niraj.stream.parser.domain.medicare.claim;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.niraj.stream.parser.GraphGenerator;
import org.niraj.stream.parser.configuration.ConfigReader;
import org.niraj.stream.parser.constants.ClaimGraphConstants;
import org.niraj.stream.parser.domain.medicare.pojo.Claim;
import org.niraj.stream.parser.domain.medicare.pojo.Patient;
import org.niraj.stream.parser.domain.medicare.repository.DiagnosisRepository;
import org.niraj.stream.parser.domain.medicare.repository.PatientRepository;
import org.niraj.stream.parser.domain.medicare.repository.ProcedureRepository;
import org.niraj.stream.parser.enumerator.EdgeType;
import org.niraj.stream.parser.pojo.Edge;
import org.niraj.stream.parser.pojo.GraphProperty;
import org.niraj.stream.parser.pojo.StreamConfigurationPattern;
import org.niraj.stream.parser.pojo.Vertex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class ClaimGraphGenerator extends GraphGenerator {

    private static final String EXCHANGE_TYPE = "direct";
    private static final String GRAPH_EXCHANGE_PROPERTY = "graph-exchange";
    private static final String QUEUE_CONNECTION_HOST_PROPERTY = "connection-host";
    private static final String QUEUE_USERNAME_PROPERTY = "rabbitmq-username";
    private static final String QUEUE_PASSWORD_PROPERTY = "rabbitmq-password";
    private static final String QUEUE_VIRTUAL_HOST_PROPERTY = "rabbitmq-vhost";
    private static final String PROCESSED_ITEM_SIZE_PROPERTY = "processed-item-size";
    private static final String SAMPLE_SIZE_PROPERTY = "sample-size";

    private static final Logger LOGGER = Logger.getLogger(ClaimGraphGenerator.class.getName());

    private ConfigReader configReader;

    private Map<String, String> patientsParsed;

    private boolean differentXP;

    private static ClaimGraphGenerator instance = null;
    private Map<String, Vertex> previousPatient;
    private Integer visitCount;
    private Integer currentGraphVertexIndex;
    private Integer currentGraphEdgeIndex;
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

    private Connection connection;
    private Channel channel;

    public ClaimGraphGenerator() throws IOException, TimeoutException {

        this.patientsParsed = new HashMap<>();
        this.resetCounters();
        this.streamConfigurationPattern = new StreamConfigurationPattern();
        this.patientRepo = PatientRepository.getInstance();
        this.diagnosisRepo = DiagnosisRepository.getInstance();
        this.procedureRepo = ProcedureRepository.getInstance();
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
        this.configReader = ConfigReader.getInstance();
        this.differentXP = new Boolean(this.configReader.getProperty(POSITIVE_GRAPH_PROPERTY));
        this.previousPatient = new HashMap<>();


        //Settings to create connection and establish channel
        _setConnection();
        channel = _getChannel();
    }

    public void closeQueueConnection() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

//    public static ClaimGraphGenerator getInstance() throws IOException, TimeoutException {
//        if (instance == null) {
//            instance = new ClaimGraphGenerator();
//        }
//        return instance;
//    }

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

    public Map<String, String> getPatientsParsed() {
        return this.patientsParsed;
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

            Vertex claimVertex = createClaimVertex(claim.getId(), subGraphId);

            createEdge(patientVertex, claimVertex, EdgeType.DIRECTED, "files", subGraphId);

            Vertex carrierVertex = createCarrierVertex(subGraphId);
            createEdge(claimVertex, carrierVertex, EdgeType.UNDIRECTED, "type", subGraphId);

            createDiagnosisPhysicianGraph(claim, carrierVertex, patientVertex, subGraphId);
        }
    }

    private void resetCounters() {
        this.visitCount = INITIAL_COUNT;
        if (this.currentGraphVertexIndex == null) {
            this.currentGraphVertexIndex = INITIAL_COUNT;
            this.currentGraphEdgeIndex = INITIAL_COUNT;
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

    private Vertex createVertex(Map<String, String> attributes) {
        Vertex vertex = new Vertex();
        vertex.setId(Integer.toString(this.currentGraphVertexIndex++));
        attributes.put(ID_INDEX, vertex.getId());
        vertex.setAttributes(attributes);


        vertexList.add(vertex);

        _publishToQueue(vertex);
        return vertex;
    }

    private Edge createEdge(Vertex source, Vertex destination, EdgeType edgeType, String label, String graphId) {
        Edge edge = new Edge();
        edge.setId(Integer.toString(this.currentGraphEdgeIndex++));
        edge.setSource(source.getId());
        edge.setTarget(destination.getId());
        edge.setDirected(EdgeType.DIRECTED == edgeType);

        Map<String, String> attributes = this.getAttribute(label, graphId);
        attributes.put(GRAPH_EDGE_SOURCE_INDEX, edge.getSource());
        attributes.put(GRAPH_EDGE_TARGET_INDEX, edge.getTarget());
        attributes.put(ID_INDEX, edge.getId());

        edge.setAttributes(attributes);

        edgeList.add(edge);

        _publishToQueue(edge); //To publish to the queue

        return edge;
    }

    private Map<String, String> getAttribute(String label, String graphId) {
        Map<String, String> vertexAttributes = new HashMap<>();
        vertexAttributes.put(GRAPH_ID_ATTRIBUTE_INDEX, graphId);
        vertexAttributes.put(LABEL_ATTRIBUTE_INDEX, label);

        return vertexAttributes;
    }

//  Related to starting the channel for producer
//

    private Channel _getChannel() throws IOException {
        Float processedItemConfig = Float.parseFloat(configReader.getProperty(PROCESSED_ITEM_SIZE_PROPERTY));
        int sampleSize = Integer.parseInt(configReader.getProperty(SAMPLE_SIZE_PROPERTY));
        Double processWindowSize = Math.ceil(sampleSize * processedItemConfig);
        int channelLimit = processWindowSize.intValue();
        String exchange = configReader.getProperty(GRAPH_EXCHANGE_PROPERTY);
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchange, EXCHANGE_TYPE);
        channel.queueDeclare(configReader.getProperty("message-queue"), false, false, false, null);
        channel.basicQos(channelLimit, true);
        return channel;
    }

    private void _publishToQueue(GraphProperty prop) {
        ObjectMapper mapper = new ObjectMapper();
        String message;
        try {
            message = mapper.writeValueAsString(prop);
        } catch (JsonProcessingException e) {
            message = prop.toString();
        }
        try {
            channel.basicPublish("", configReader.getProperty("message-queue"), null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _setConnection() throws TimeoutException, IOException {
        if (connection == null) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername(configReader.getProperty(QUEUE_USERNAME_PROPERTY));
            factory.setPassword(configReader.getProperty(QUEUE_PASSWORD_PROPERTY));
            factory.setHost(configReader.getProperty(QUEUE_CONNECTION_HOST_PROPERTY));
            factory.setVirtualHost(configReader.getProperty(QUEUE_VIRTUAL_HOST_PROPERTY));
            int oneHourHeartBeat = 600 * 61;
            factory.setRequestedHeartbeat(oneHourHeartBeat);
            connection = factory.newConnection();
        }

    }
}
