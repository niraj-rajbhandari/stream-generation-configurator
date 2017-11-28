package org.niraj.stream.parser.domain.medicare.pojo;

public class Procedure {


    private String procedure;
    private String lowHCPCSCode;
    private String highHCPCSCode;
    private String type;

    public Procedure(String procedure, String lowHCPCSCode, String highHCPCSCode, String type) {
        this.setProcedure(procedure);
        this.setLowHCPCSCode(lowHCPCSCode);
        this.setHighHCPCSCode(highHCPCSCode);
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getLowHCPCSCode() {
        return lowHCPCSCode;
    }

    public void setLowHCPCSCode(String lowHCPCSCode) {
        this.lowHCPCSCode = lowHCPCSCode;
    }

    public String getHighHCPCSCode() {
        return highHCPCSCode;
    }

    public void setHighHCPCSCode(String highHCPCSCode) {
        this.highHCPCSCode = highHCPCSCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Procedure{" +
                "procedure='" + procedure + '\'' +
                ", lowHCPCSCode=" + lowHCPCSCode +
                ", highHCPCSCode=" + highHCPCSCode +
                '}';
    }
}
