package org.niraj.stream.generator.domain.medicare.claim;

import org.niraj.stream.generator.configuration.ConfigReader;
import org.niraj.stream.generator.configuration.DataStreamConfigurator;
import org.niraj.stream.generator.domain.medicare.pojo.Claim;
import org.niraj.stream.generator.domain.medicare.pojo.Patient;
import org.niraj.stream.generator.domain.medicare.repository.PatientRepository;
import org.niraj.stream.generator.helper.Helper;
import org.niraj.stream.generator.pojo.StreamConfiguration;

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
    public void createDataStreamConfiguration() throws FileNotFoundException, IllegalArgumentException {
        StreamConfiguration configuration = new StreamConfiguration();
        ClaimGraphGenerator graphGenerator = ClaimGraphGenerator.getInstance();
        PatientRepository patientRepo = PatientRepository.getInstance();
        Iterator<Claim> csvIterator = this.csvParser.iterator();
        int count = 0;
        while (csvIterator.hasNext() && count < 10) {
            Claim cp = csvIterator.next();

        }
        // configuration.setPatterns(graphGenerator.getGraphStream());
    }
}
