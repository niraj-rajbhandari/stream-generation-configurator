package org.niraj.stream.generator.enumerator;

public enum EdgeType {
    DIRECTED("d"),
    UNDIRECTED("u");

    private String edgeType;

    EdgeType(String edgeType) {
        this.setEdgeType(edgeType);
    }

    public void setEdgeType(String edgeType) {
        this.edgeType = edgeType;
    }

    public String getEdgeType() {
        return this.edgeType;
    }
}
