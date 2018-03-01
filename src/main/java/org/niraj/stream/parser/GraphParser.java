package org.niraj.stream.parser;

import org.niraj.stream.parser.configuration.ConfigReader;
import org.niraj.stream.parser.configuration.DataStreamConfigurator;
import org.niraj.stream.parser.domain.app.AppStreamConfigurator;
import org.niraj.stream.parser.domain.email.EnronEmailStreamConfigurator;
import org.niraj.stream.parser.domain.email.pojo.Email;
import org.niraj.stream.parser.domain.medicare.claim.ClaimDataStreamConfigurator;
import org.niraj.stream.parser.domain.medicare.pojo.Claim;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class GraphParser {

    private static final String INPUT_DATA_KEY = "input-data";
    private static final String OUTPUT_FILE = "output-file";
    private static final String DATA_TYPE_KEY = "data-type";
    private static final String DATA_TYPE_EMAIL = "email";
    private static final String DATA_TYPE_MEDICARE = "medicare";
    private static final String DATA_TYPE_APPLICATION = "application_processing";

    public static void main(String... args) {
        try {
            String dataType = ConfigReader.getInstance().getProperty(DATA_TYPE_KEY);
            if (dataType.equals(DATA_TYPE_EMAIL)) {
                GraphParser.enronGraphGenerator(args);
            } else if (dataType.equals(DATA_TYPE_MEDICARE)) {
                GraphParser.claimGraphGenerator();
            } else if (dataType.equals(DATA_TYPE_APPLICATION)) {
                System.out.println("Application Processing");
                GraphParser.appProcessingGraphGenerator();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void claimGraphGenerator(String... args) {
        try {
            ConfigReader configReader = ConfigReader.getInstance();
            DataStreamConfigurator<Claim> claimDataStreamConfigurator =
                    new ClaimDataStreamConfigurator(configReader.getProperty(INPUT_DATA_KEY),
                            configReader.getProperty(OUTPUT_FILE));
            claimDataStreamConfigurator.createDataStreamConfiguration();

//            System.out.println("total items: " + claimDataStreamConfigurator.getConfiguration().getPatterns().stream()
//                    .mapToInt(p -> (p.getVertices().size() + p.getEdges().size())).sum());
            claimDataStreamConfigurator.createJson();

        } catch (IOException | IllegalArgumentException | IllegalAccessException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void enronGraphGenerator(String... args) {
        try {
            ConfigReader configReader = ConfigReader.getInstance();
            DataStreamConfigurator<Email> configurator = new EnronEmailStreamConfigurator(
                    configReader.getProperty(OUTPUT_FILE));
            configurator.createDataStreamConfiguration();
        } catch (IOException | TimeoutException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void appProcessingGraphGenerator(String... args) {
        try {
            ConfigReader configReader = ConfigReader.getInstance();
            DataStreamConfigurator<String> appDataStreamConfigurator =
                    new AppStreamConfigurator(configReader.getProperty(INPUT_DATA_KEY),
                            configReader.getProperty(OUTPUT_FILE));
            appDataStreamConfigurator.createDataStreamConfiguration();
            appDataStreamConfigurator.createJson();

        } catch (IOException | IllegalArgumentException | IllegalAccessException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> parseArgs(String... args) {
        Map<String, String> arguments = new HashMap<>();
        System.out.println("args length: " + args.length);
        if (args.length != 2) {
            usage();
            return null;
        }
        int counter = 0;
        while (counter < args.length) {
            String argument = args[counter];
            counter++;
            if (counter >= args.length) {
                usage();
                throw new RuntimeException("Please provide the value to the argument");
            }
            if (argument.equals("-c") || argument.equals("--config")) {
                arguments.put("config", args[counter]);
            }
            counter++;
        }
        return arguments;
    }

    private static void usage() {
        System.out.println("Usage");
        System.out.println("=====");
        System.out.println("java -jar {path/to/jar} [options]");
        System.out.println("Options");
        System.out.println("-------");
        System.out.println("-c | --config {absolute/path/to/config/file}");
    }
}
