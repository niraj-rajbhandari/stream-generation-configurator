package org.niraj.stream.generator.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Vertex extends GraphProperty{

    private String id;

    @JsonProperty("new")
    private boolean isNew;

    private Map<String, String> attributes;

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
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
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
