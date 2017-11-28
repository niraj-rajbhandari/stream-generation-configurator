package org.niraj.stream.parser.io;

import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;
import org.niraj.stream.parser.FilterType;

public class CsvFilter<T> implements CsvToBeanFilter {

    private final MappingStrategy<T> strategy;
    private String column;
    private String value;
    private FilterType filterType;

    public CsvFilter(MappingStrategy<T> strategy, String column, String value, FilterType filterType) {
        this.strategy = strategy;
        this.value = value;
        this.column = column;
        this.filterType = filterType;

    }

    public boolean allowLine(String[] line) {
        int columnIndex = strategy.getColumnIndex(column);
        String columnValue = line[columnIndex].trim();
        return filter(columnValue);
    }

    private boolean filter(String columnValue) {
        switch (filterType) {
            case STARTS_WITH:
                return columnValue.startsWith(value);
            case EQUALS:
                return columnValue.equals(value);
            default:
                return Integer.parseInt(columnValue) == Integer.parseInt(value);
        }
    }

    @Override
    public String toString() {
        return "CsvFilter{" +
                "strategy=" + strategy +
                ", column='" + column + '\'' +
                ", value='" + value + '\'' +
                ", filterType=" + filterType +
                '}';
    }
}
