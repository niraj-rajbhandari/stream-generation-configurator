package org.niraj.stream.parser.domain.app;

import org.niraj.stream.parser.configuration.DataStreamConfigurator;
import org.niraj.stream.parser.message.queue.QueueService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppStreamConfigurator extends DataStreamConfigurator<String> {
    public AppStreamConfigurator(String inputFile, String outputFile) throws IOException {
        super(inputFile, outputFile);
    }

    @Override
    public void createDataStreamConfiguration() throws IOException, IllegalArgumentException, IllegalAccessException,
            TimeoutException {
        try (Stream<String> stream = Files.lines(Paths.get(inputFile))) {
            List<String> fileContent = stream.collect(Collectors.toList());
            AppGraphGenerator graphGenerator = new AppGraphGenerator();
            for (String line : fileContent) {
                graphGenerator.createGraphStream(line);
            }
            this.getConfiguration().setPatterns(graphGenerator.getGraphStream());

            QueueService queueService = QueueService.getInstance();
            queueService.publishToQueue(PUBLISHING_COMPLETED);
            QueueService.closeQueue();
            this.createJson();
        }
    }
}
