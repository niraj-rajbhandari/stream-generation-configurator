package org.niraj.stream.generator.pojo;

import org.niraj.stream.generator.helper.Helper;

import java.util.List;

public class StreamConfiguration {
    public static final int DEFAULT_STREAM_ID = 1;

    private int numStreams;
    private int secondsPerUnitTime;
    private String startTime;
    private int duration;
    private String outputTimeFormat;
    private String outputFilePrefix;
    private List<StreamConfigurationPattern> patterns;

    public StreamConfiguration() {
        numStreams = 1;
        secondsPerUnitTime = 1;
        startTime = Helper.getInstance().getTimeInFuture("YYYY-MM-DD HH:MM:SS", 10);
        duration = 10;
        outputTimeFormat = "units";
        outputFilePrefix = "carrier_claim_";
    }

    public int getNumStreams() {
        return numStreams;
    }

    public void setNumStreams(int numStreams) {
        this.numStreams = numStreams;
    }

    public int getSecondsPerUnitTime() {
        return secondsPerUnitTime;
    }

    public void setSecondsPerUnitTime(int secondsPerUnitTime) {
        this.secondsPerUnitTime = secondsPerUnitTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getOutputTimeFormat() {
        return outputTimeFormat;
    }

    public void setOutputTimeFormat(String outputTimeFormat) {
        this.outputTimeFormat = outputTimeFormat;
    }

    public String getOutputFilePrefix() {
        return outputFilePrefix;
    }

    public void setOutputFilePrefix(String outputFilePrefix) {
        this.outputFilePrefix = outputFilePrefix;
    }

    public List<StreamConfigurationPattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<StreamConfigurationPattern> patterns) {
        this.patterns = patterns;
    }

    @Override
    public String toString() {
        return "StreamConfiguration{" +
                "numStreams=" + numStreams +
                ", secondsPerUnitTime=" + secondsPerUnitTime +
                ", startTime='" + startTime + '\'' +
                ", duration=" + duration +
                ", outputTimeFormat='" + outputTimeFormat + '\'' +
                ", outputFilePrefix='" + outputFilePrefix + '\'' +
                ", patterns=" + patterns +
                '}';
    }
}
