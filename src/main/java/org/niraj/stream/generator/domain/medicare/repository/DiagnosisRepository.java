package org.niraj.stream.generator.domain.medicare.repository;

import org.niraj.stream.generator.domain.medicare.pojo.Diagnosis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiagnosisRepository {

    private List<Diagnosis> diagnosisList = null;
    private static DiagnosisRepository ourInstance = new DiagnosisRepository();

    public static DiagnosisRepository getInstance() {
        return ourInstance;
    }

    private DiagnosisRepository() {
        this.setDiagnosisList();
    }

    public void setDiagnosisList() {
        if (diagnosisList == null) {
            diagnosisList = new ArrayList<>();
            diagnosisList.add(new Diagnosis("nervous", "320", "359"));
            diagnosisList.add(new Diagnosis("sense organs", "360", "389"));
            diagnosisList.add(new Diagnosis("circulatory", "390", "459"));
            diagnosisList.add(new Diagnosis("respiratory", "460", "519"));
            diagnosisList.add(new Diagnosis("digestive", "520", "579"));
            diagnosisList.add(new Diagnosis("genitourinary", "580", "629"));
            diagnosisList.add(new Diagnosis("pregnancy", "630", "679"));
            diagnosisList.add(new Diagnosis("skin", "680", "709"));
            diagnosisList.add(new Diagnosis("musculoskeletal", "710", "739"));
            diagnosisList.add(new Diagnosis("congenital", "740", "759"));
            diagnosisList.add(new Diagnosis("perinatal", "760", "779"));
            diagnosisList.add(new Diagnosis("symptoms", "780", "799"));
            diagnosisList.add(new Diagnosis("injury & poison", "800", "999"));
            diagnosisList.add(new Diagnosis("infectious & parasitic", "001", "139"));
            diagnosisList.add(new Diagnosis("neoplasms", "140", "239"));
            diagnosisList.add(new Diagnosis("endocrine", "140", "279"));
            diagnosisList.add(new Diagnosis("blood", "280", "289"));
            diagnosisList.add(new Diagnosis("mental", "290", "319"));
            diagnosisList.add(new Diagnosis("nervous", "320", "359"));
        }
    }

    public List<Diagnosis> getDiagnosisList() {
        return diagnosisList;
    }

    public String find(String diagnosisCode) {
        if (diagnosisCode.matches("[A-Za-z0-9]+")) {
            return (Character.isAlphabetic(diagnosisCode.charAt(0)))
                    ? Character.toString(diagnosisCode.charAt(0))
                    : "Other";
        } else {
            int dCode = Integer.parseInt(diagnosisCode.substring(0, 3));
            List<Diagnosis> filteredList = diagnosisList.stream()
                    .filter(d -> Integer.parseInt(d.getLowICDCode()) <= dCode
                            && Integer.parseInt(d.getHighICDCode()) >= dCode)
                    .collect(Collectors.toList());

            return (filteredList.size() > 0) ? filteredList.get(0).getDiagnosis() : "Other";
        }
    }
}
