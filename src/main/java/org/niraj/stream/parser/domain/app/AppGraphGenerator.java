package org.niraj.stream.parser.domain.app;

import org.niraj.stream.parser.GraphGenerator;
import org.niraj.stream.parser.enumerator.EdgeType;
import org.niraj.stream.parser.pojo.Edge;
import org.niraj.stream.parser.pojo.Vertex;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class AppGraphGenerator extends GraphGenerator<String> {

    private static final String VERTEX_INDICATOR = "v";
    private static final String DIRECTED_EDGE_INDICATOR = "d";
    private static final String UNDIRECTED_EDGE_INDICATOR = "u";
    private static final String XP_INDICATOR = "XP";
    private static final String COMMENT_INDICATOR = "//";

    private String graphId;
    private String comment;

    private Map<Integer, Vertex> vertexMapper;

    public AppGraphGenerator() throws IOException, TimeoutException {
        super();
        vertexMapper = new HashMap<>();
        comment = "";
    }

    @Override
    public void createGraphStream(String s) throws IOException {
        List<String> splittedString = new ArrayList<>(Arrays.asList(s.replace("\n", "").split(" ")));
        String lineTypeIndicator = splittedString.get(0);
        String label;
        if (lineTypeIndicator.equals(XP_INDICATOR)) {
            graphId = splittedString.get(2);
        } else if (lineTypeIndicator.equals(VERTEX_INDICATOR)) {
            label = splittedString.get(2).replace("\"", "");
            Map<String, String> vertexAttributes = getAttribute(label, graphId);
            if (!comment.isEmpty()) {
                vertexAttributes.put(COMMENT_ATTRIBUTE_INDEX, comment);
                comment = "";
            }
            Vertex vertex = createVertex(vertexAttributes);
            vertexMapper.put(Integer.parseInt(splittedString.get(1)), vertex);
        } else if (lineTypeIndicator.equals(UNDIRECTED_EDGE_INDICATOR)) {
            label = splittedString.get(3).replace("\"", "");
            Vertex sourceVertex = vertexMapper.get(Integer.parseInt(splittedString.get(1)));
            Vertex targetVertex = vertexMapper.get(Integer.parseInt(splittedString.get(2)));

            Map<String, String> attributes = getAttribute(label, graphId);

            if (!comment.isEmpty()) {
                attributes.put(COMMENT_ATTRIBUTE_INDEX, comment);
                comment = "";
            }

            Edge edge = createEdge(sourceVertex, targetVertex, EdgeType.UNDIRECTED, attributes);

        } else if (lineTypeIndicator.equals(DIRECTED_EDGE_INDICATOR)) {
            label = splittedString.get(3).replace("\"", "");
            Vertex sourceVertex = vertexMapper.get(Integer.parseInt(splittedString.get(1)));
            Vertex targetVertex = vertexMapper.get(Integer.parseInt(splittedString.get(2)));

            Edge edge = createEdge(sourceVertex, targetVertex, EdgeType.DIRECTED, label, graphId);
        } else if (lineTypeIndicator.equals(COMMENT_INDICATOR)) {
            comment = s;
        }
    }
}
