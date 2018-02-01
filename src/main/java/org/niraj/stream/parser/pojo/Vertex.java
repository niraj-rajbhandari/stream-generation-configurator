package org.niraj.stream.parser.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Vertex extends GraphProperty{

    private String id;

    @JsonProperty("new")
    private boolean isNew;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Vertex(){
        setNew(true);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + id +
                ", isNew=" + isNew +
                ", attributes=" + attributes +
                '}';
    }
}
