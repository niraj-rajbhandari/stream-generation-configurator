package org.niraj.stream.generator.domain.medicare.repository;

import org.niraj.stream.generator.FilterType;
import org.niraj.stream.generator.helper.Helper;
import org.niraj.stream.generator.configuration.ConfigReader;
import org.niraj.stream.generator.domain.medicare.pojo.Patient;
import org.niraj.stream.generator.io.CsvParser;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PatientRepository {

    private String patientFile;
    private List<Patient> patientList = null;
    private static PatientRepository instance = null;
    private Helper helper;

    /**
     * Filter patients by state code given in config.properties
     */
    private Map<String, String> patientsFilterByState;

    public static PatientRepository getInstance() throws FileNotFoundException, IllegalArgumentException {
        ConfigReader configReader = ConfigReader.getInstance();
        String patientFile = configReader.getProperty("patient-data");
        if (instance == null) {
            instance = new PatientRepository(patientFile);
        }
        return instance;
    }

    public Patient findById(String patientId) {
        List<Patient> searchedPatient = this.patientList.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .collect(Collectors.toList());
        return (searchedPatient.size() > 0) ? searchedPatient.get(0) : null;
    }

    public List<Patient> findAll() {
        return this.patientList;
    }

    public boolean patientExists(String patiendId) {
        return (this.findById(patiendId) != null);
    }

    private PatientRepository(String patientFile)
            throws FileNotFoundException, IllegalArgumentException {
        helper = Helper.getInstance();
        patientsFilterByState = new HashMap<>();
        patientsFilterByState.put(Patient.STATE_CODE_INDEX,
                ConfigReader.getInstance().getProperty(Patient.STATE_CODE_PROPERTY_INDEX));

        this.patientFile = helper.getAbsolutePath(patientFile);
        this.setPatientList();
    }

    private void setPatientList() throws FileNotFoundException, IllegalArgumentException {
        if (patientList == null) {
            CsvParser<Patient> csvParser = new CsvParser<>(this.patientFile, Patient.class,
                    patientsFilterByState, FilterType.EQUALS);

            patientList = csvParser.parse();
        }
    }
}
