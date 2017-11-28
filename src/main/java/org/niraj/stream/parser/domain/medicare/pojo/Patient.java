package org.niraj.stream.parser.domain.medicare.pojo;

import com.opencsv.bean.CsvBindByName;

import java.util.Objects;

public class Patient {

    public static final String STATE_CODE_PROPERTY_INDEX = "state-code";
    public static final String STATE_CODE_INDEX = "SP_STATE_CODE";

    @CsvBindByName(required = true, column = "DESYNPUF_ID")//patientId
    private String patientId;

    @CsvBindByName(required=true, column = "BENE_BIRTH_DT")
    private String dob;

    @CsvBindByName(required = true, column = "SP_STATE_CODE")
    private String state;

    @CsvBindByName(required = true, column = "BENE_SEX_IDENT_CD")
    private String gender;

    @CsvBindByName(required = true, column = "BENE_RACE_CD")
    private String race;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(patientId, patient.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", dob='" + dob + '\'' +
                ", state='" + state + '\'' +
                ", gender='" + gender + '\'' +
                ", race='" + race + '\'' +
                '}';
    }
}
