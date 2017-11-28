package org.niraj.stream.parser.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Edge extends GraphProperty {

    private String id;
    private String source;
    private String target;
    private boolean isDirected;
    private int minOffset;
    private int maxOffset;

    @JsonProperty("streamNum")
    private int streamId;

    private Map<String,String> attributes;

    public Edge(){
        setMinOffset(10);
        setMaxOffset(30);
        setStreamId(StreamConfiguration.DEFAULT_STREAM_ID);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public void setDirected(boolean directed) {
        isDirected = directed;
    }

    public int getMinOffset() {
        return minOffset;
    }

    public void setMinOffset(int minOffset) {
        this.minOffset = minOffset;
    }

    public int getMaxOffset() {
        return maxOffset;
    }

    public void setMaxOffset(int maxOffset) {
        this.maxOffset = maxOffset;
    }

    public int getStreamId() {
        return streamId;
    }

    public void setStreamId(int streamId) {
        this.streamId = streamId;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }


}
