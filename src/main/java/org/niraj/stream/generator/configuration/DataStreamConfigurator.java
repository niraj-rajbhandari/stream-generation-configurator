package org.niraj.stream.generator.configuration;

import org.niraj.stream.generator.io.CsvParser;

import java.io.FileNotFoundException;

public abstract class DataStreamConfigurator<T> {

    protected String inputFile;
    protected String outputFile;
    protected CsvParser<T> csvParser;


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

    public abstract void createDataStreamConfiguration() throws FileNotFoundException, IllegalArgumentException;
}
