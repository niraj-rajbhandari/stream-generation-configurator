package org.niraj.stream.generator.domain.medicare.claim;

import org.niraj.stream.generator.configuration.ConfigReader;
import org.niraj.stream.generator.configuration.DataStreamConfigurator;
import org.niraj.stream.generator.domain.medicare.pojo.Claim;
import org.niraj.stream.generator.helper.Helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;

public class ClaimDataStreamConfigurator extends DataStreamConfigurator<Claim> {

    public ClaimDataStreamConfigurator(String inputFile, String outputFile)
            throws FileNotFoundException {
        this.setInputFile(Helper.getInstance().getAbsolutePath(inputFile));
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
//            checkSize =
//                    graphGenerator.getPatientsParsed().size() < Integer.parseInt(ConfigReader.getInstance().getProperty("graph-size"));
//            System.out.println("checkSize: " + checkSize);
            checkSize = true;
        }
        this.getConfiguration().setPatterns(graphGenerator.getGraphStream());
    }
}
