package org.niraj.stream.parser.pojo;

import java.util.List;

public class StreamConfigurationPattern {
    private int id;
    private boolean track;
    private float probability;
    private List<Vertex> vertices;
    private List<Edge> edges;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTrack() {
        return track;
    }

    public void setTrack(boolean track) {
        this.track = track;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    @Override
    public String toString() {
        return "StreamConfigurationPattern{" +
                "id=" + id +
                ", track=" + track +
                ", probability=" + probability +
                ", vertices=" + vertices +
                ", edges=" + edges +
                '}';
    }
}
