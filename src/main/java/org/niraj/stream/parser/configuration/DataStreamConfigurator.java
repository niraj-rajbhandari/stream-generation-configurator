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

    protected static final String PUBLISHING_COMPLETED = "done";

    protected String inputFile;
    protected String outputFile;
    protected CsvParser<T> csvParser;
    protected StreamConfiguration configuration = new StreamConfiguration();

    protected ConfigReader config;
    protected Helper helper;


    protected DataStreamConfigurator(String inputFile, String outputFile) throws IOException {
        config = ConfigReader.getInstance();
        helper = Helper.getInstance();
        helper.setDataDirectory(config.getProperty(Helper.DATA_TYPE_KEY));
        if (inputFile != null)
            this.setInputFile(helper.getAbsolutePath(inputFile, true));
        else
            this.setInputFile(null);

        this.setOutputFile(outputFile);
    }

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
        if (this.csvParser != null)
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
        File output = new File(helper.getAbsolutePath(outputFile, true));
        jsonMapper.writeValue(output, this.configuration);
    }
}
