package org.niraj.stream.generator.configuration;

import com.google.gson.Gson;
import org.niraj.stream.generator.helper.Helper;
import org.niraj.stream.generator.io.CsvParser;
import org.niraj.stream.generator.pojo.StreamConfiguration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
            throws FileNotFoundException, IllegalArgumentException, IllegalAccessException;

    public void createJson() throws IOException {
        Gson gson = new Gson();
        FileWriter jsonWriter = new FileWriter(Helper.getInstance().getAbsolutePath(outputFile));
        gson.toJson(configuration,jsonWriter);
    }
}
