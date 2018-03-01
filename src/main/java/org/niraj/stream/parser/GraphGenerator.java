package org.niraj.stream.parser;

import org.niraj.stream.parser.configuration.ConfigReader;
import org.niraj.stream.parser.enumerator.EdgeType;
import org.niraj.stream.parser.message.queue.QueueService;
import org.niraj.stream.parser.pojo.Edge;
import org.niraj.stream.parser.pojo.StreamConfigurationPattern;
import org.niraj.stream.parser.pojo.Vertex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public abstract class GraphGenerator<T> {
    protected static final String LABEL_ATTRIBUTE_INDEX = "label";
    protected static final String COMMENT_ATTRIBUTE_INDEX = "comment";
    protected static final String GRAPH_ID_ATTRIBUTE_INDEX = "graph_id";
    protected static final int INITIAL_COUNT = 1;
    protected static final String CODE_INDEX = "code";
    protected static final String ID_INDEX = "id";
    protected static final String GRAPH_EDGE_SOURCE_INDEX = "source";
    protected static final String GRAPH_EDGE_TARGET_INDEX = "target";
    protected static final String POSITIVE_GRAPH_PROPERTY = "positive-graph";
    protected static final String SINGLE_GRAPH = "1";

    protected ConfigReader configReader;

    protected Integer currentGraphVertexIndex;
    protected Integer currentGraphEdgeIndex;
    protected Integer visitCount;

    protected List<Vertex> vertexList;
    protected List<Edge> edgeList;
    protected QueueService queueService;

    protected boolean differentGraphs;

    private StreamConfigurationPattern streamConfigurationPattern;

    protected GraphGenerator() throws IOException, TimeoutException {
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
        this.configReader = ConfigReader.getInstance();
        this.resetCounters();
        queueService = QueueService.getInstance();

        this.streamConfigurationPattern = new StreamConfigurationPattern();
    }

    public boolean isDifferentGraphs() {
        return differentGraphs;
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

    protected Vertex createVertex(Map<String, String> attributes) {
        Vertex vertex = new Vertex();
        vertex.setId(Integer.toString(this.currentGraphVertexIndex++));
        attributes.put(ID_INDEX, vertex.getId());
        vertex.setAttributes(attributes);


        vertexList.add(vertex);

        queueService.publishGraphToQueue(vertex);
        return vertex;
    }

    protected Edge createEdge(Vertex source, Vertex destination, EdgeType edgeType, Map<String, String> attributes){
        Edge edge = new Edge();
        edge.setId(Integer.toString(this.currentGraphEdgeIndex++));
        edge.setSource(source.getId());
        edge.setTarget(destination.getId());
        edge.setDirected(EdgeType.DIRECTED == edgeType);

        attributes.put(GRAPH_EDGE_SOURCE_INDEX, edge.getSource());
        attributes.put(GRAPH_EDGE_TARGET_INDEX, edge.getTarget());
        attributes.put(ID_INDEX, edge.getId());

        edge.setAttributes(attributes);
        edgeList.add(edge);

        queueService.publishGraphToQueue(edge); //To publish to the queue

        return edge;
    }

    protected Edge createEdge(Vertex source, Vertex destination, EdgeType edgeType, String label, String graphId) {
        Map<String, String> attributes = this.getAttribute(label, graphId);
        return createEdge(source,destination,edgeType,attributes);
    }

    protected Map<String, String> getAttribute(String label, String graphId) {
        Map<String, String> vertexAttributes = new HashMap<>();
        vertexAttributes.put(GRAPH_ID_ATTRIBUTE_INDEX, graphId);
        vertexAttributes.put(LABEL_ATTRIBUTE_INDEX, label);

        return vertexAttributes;
    }

    protected void resetCounters() {
        this.visitCount = INITIAL_COUNT;
        if (this.currentGraphVertexIndex == null) {
            this.currentGraphVertexIndex = INITIAL_COUNT;
            this.currentGraphEdgeIndex = INITIAL_COUNT;
        }
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

    public abstract void createGraphStream(T t) throws IOException;
}
