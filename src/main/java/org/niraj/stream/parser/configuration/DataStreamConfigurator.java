package org.niraj.stream.parser.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.niraj.stream.parser.helper.Helper;
import org.niraj.stream.parser.io.CsvParser;
import org.niraj.stream.parser.pojo.StreamConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class DataStreamConfigurator<T> {

    private String inputFile;
    private String outputFile;
    private CsvParser<T> csvParser;
    private StreamConfiguration configuration = new StreamConfiguration();


    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public CsvParser<T> getCsvParser() {
        return csvParser;
    }

    public void setCsvParser(Class<T> pojoClass)
            throws FileNotFoundException {
        this.csvParser = new CsvParser<>(this.inputFile, pojoClass, null, null);
    }

    public StreamConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(StreamConfiguration configuration) {
        this.configuration = configuration;
    }

    public abstract void createDataStreamConfiguration()
            throws IOException, IllegalArgumentException, IllegalAccessException, TimeoutException;

    public void createJson() throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
//        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File output = new File(Helper.getInstance().getAbsolutePath(outputFile,true));
        jsonMapper.writeValue(output, this.configuration);
    }
}
