package org.niraj.stream.parser.domain.medicare.claim;

import org.niraj.stream.parser.configuration.DataStreamConfigurator;
import org.niraj.stream.parser.domain.medicare.pojo.Claim;
import org.niraj.stream.parser.helper.Helper;
import org.niraj.stream.parser.configuration.ConfigReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;

public class ClaimDataStreamConfigurator extends DataStreamConfigurator<Claim> {

    private ConfigReader config;
    public ClaimDataStreamConfigurator(String inputFile, String outputFile)
            throws FileNotFoundException {
        config = ConfigReader.getInstance();
        this.setInputFile(Helper.getInstance().getAbsolutePath(inputFile, true));
        this.setOutputFile(outputFile);

        this.setCsvParser(Claim.class);
    }

    @Override
    public void createDataStreamConfiguration()
            throws IOException, IllegalArgumentException, IllegalAccessException, TimeoutException {
        ClaimGraphGenerator graphGenerator = ClaimGraphGenerator.getInstance();
        Iterator<Claim> csvIterator = this.getCsvParser().iterator();
        boolean checkSize = true;

        while (csvIterator.hasNext() && checkSize) {
            Claim claim = csvIterator.next();
            graphGenerator.createGraphStream(claim);

            if (Helper.getInstance().isDebugMode(config)) {
                checkSize =
                        graphGenerator.getPatientsParsed().size() < Integer.parseInt(ConfigReader.getInstance().getProperty("graph-size"));

            } else {
                checkSize = true;
            }


        }
        this.getConfiguration().setPatterns(graphGenerator.getGraphStream());
    }
}
