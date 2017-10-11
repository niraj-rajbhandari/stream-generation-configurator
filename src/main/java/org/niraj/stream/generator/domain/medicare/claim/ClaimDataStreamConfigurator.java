package org.niraj.stream.generator.domain.medicare.claim;

import org.niraj.stream.generator.configuration.DataStreamConfigurator;
import org.niraj.stream.generator.domain.medicare.pojo.Claim;
import org.niraj.stream.generator.helper.Helper;

import java.io.FileNotFoundException;
import java.util.Iterator;

public class ClaimDataStreamConfigurator extends DataStreamConfigurator<Claim> {

    public ClaimDataStreamConfigurator(String inputFile, String outputFile)
            throws FileNotFoundException {
        this.setInputFile(Helper.getInstance().getAbsolutePath(inputFile));
        this.setOutputFile(outputFile);

        this.setCsvParser(Claim.class);
    }

    @Override
    public void createDataStreamConfiguration()
            throws FileNotFoundException, IllegalArgumentException, IllegalAccessException {
        ClaimGraphGenerator graphGenerator = ClaimGraphGenerator.getInstance();
        Iterator<Claim> csvIterator = this.getCsvParser().iterator();
        while (csvIterator.hasNext()) {
            Claim claim = csvIterator.next();
            graphGenerator.createGraphStream(claim);
        }
        this.getConfiguration().setPatterns(graphGenerator.getGraphStream());
    }
}
