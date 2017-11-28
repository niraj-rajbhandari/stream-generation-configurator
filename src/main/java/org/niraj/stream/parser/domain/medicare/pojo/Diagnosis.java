package org.niraj.stream.parser.domain.medicare.pojo;

public class Diagnosis {
    private String diagnosis;
    private String lowICDCode;
    private String highICDCode;
    
    public Diagnosis(String diagnosis, String lowICDCode, String highICDCode){
        this.setDiagnosis(diagnosis);
        this.setLowICDCode(lowICDCode);
        this.setHighICDCode(highICDCode);
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getLowICDCode() {
        return lowICDCode;
    }

    public void setLowICDCode(String lowICDCode) {
        this.lowICDCode = lowICDCode;
    }

    public String getHighICDCode() {
        return highICDCode;
    }

    public void setHighICDCode(String highICDCode) {
        this.highICDCode = highICDCode;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "diagnosis='" + diagnosis + '\'' +
                ", lowICDCode=" + lowICDCode +
                ", highICDCode=" + highICDCode +
                '}';
    }
}
