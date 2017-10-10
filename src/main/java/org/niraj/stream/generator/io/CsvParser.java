package org.niraj.stream.generator.io;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.MappingStrategy;
import org.niraj.stream.generator.FilterType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CsvParser<T> {

    private FileReader fileReader;
    private CSVReader csvReader;
    private CsvToBean<T> csvToBean;


    public CsvParser(String inputFile, Class<T> pojoClass, Map<String, String> filter, FilterType filterType)
            throws FileNotFoundException {
        try {
            this.fileReader = new FileReader(inputFile);
            this.csvReader = new CSVReader(this.fileReader);
            csvToBean = new CsvToBean<>();
            csvToBean.setCsvReader(this.csvReader);
            MappingStrategy<T> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
            mappingStrategy.setType(pojoClass);
            if (filter != null) {
                csvToBean.setFilter(getFilter(mappingStrategy, filter, filterType));
            }
            csvToBean.setMappingStrategy(mappingStrategy);

        } catch (IOException e) {
            this.close();
            e.printStackTrace();
            throw new FileNotFoundException("File: " + inputFile + " not found");
        }
    }

    public Iterator<T> iterator() {
        return csvToBean.iterator();
    }

    public List<T> parse() {
        return csvToBean.parse();
    }

    public void close() {
        try {
            if (this.csvReader != null)
                this.csvReader.close();
            if (this.fileReader != null)
                this.fileReader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public CsvToBeanFilter getFilter(MappingStrategy<T> strategy, Map<String, String> filter, FilterType filterType) {
        if (filter.size() == 0 || filter.size() > 1) {
            throw new IllegalArgumentException("Only one filter condition is allowed");
        }
        String columnName = "";
        String columnValue = "";
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            columnName = entry.getKey();
            columnValue = entry.getValue();
        }
        return new CsvFilter<>(strategy, columnName, columnValue, filterType);
    }
}
