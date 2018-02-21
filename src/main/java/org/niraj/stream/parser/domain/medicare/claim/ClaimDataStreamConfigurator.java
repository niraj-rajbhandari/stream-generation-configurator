package org.niraj.stream.parser.domain.medicare.claim;

import org.niraj.stream.parser.configuration.ConfigReader;
import org.niraj.stream.parser.configuration.DataStreamConfigurator;
import org.niraj.stream.parser.domain.medicare.pojo.Claim;
import org.niraj.stream.parser.helper.Helper;
import org.niraj.stream.parser.message.queue.QueueService;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;

public class ClaimDataStreamConfigurator extends DataStreamConfigurator<Claim> {

    public ClaimDataStreamConfigurator(String inputFile, String outputFile)
            throws IOException {
        super(inputFile,outputFile);
        this.setCsvParser(Claim.class);
    }

    @Override
    public void createDataStreamConfiguration()
            throws IOException, IllegalArgumentException, IllegalAccessException, TimeoutException {
        ClaimGraphGenerator graphGenerator = new ClaimGraphGenerator();
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

        QueueService queueService = QueueService.getInstance();
        queueService.publishToQueue(PUBLISHING_COMPLETED);
        QueueService.closeQueue();
    }
}
