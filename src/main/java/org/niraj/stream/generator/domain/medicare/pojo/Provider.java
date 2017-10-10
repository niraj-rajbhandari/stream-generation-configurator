package org.niraj.stream.generator.domain.medicare.pojo;

public class Provider {

    private String institute;
    private String lowPhysicianId;
    private String highPhysicianId;

    public Provider(String institute, String lowPhysicianId, String highPhysicianId) {
        this.setInstitute(institute);
        this.setLowPhysicianId(lowPhysicianId);
        this.setHighPhysicianId(highPhysicianId);
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getLowPhysicianId() {
        return lowPhysicianId;
    }

    public void setLowPhysicianId(String lowPhysicianId) {
        this.lowPhysicianId = lowPhysicianId;
    }

    public String getHighPhysicianId() {
        return highPhysicianId;
    }

    public void setHighPhysicianId(String highPhysicianId) {
        this.highPhysicianId = highPhysicianId;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "institute='" + institute + '\'' +
                ", lowPhysicianId='" + lowPhysicianId + '\'' +
                ", highPhysicianId='" + highPhysicianId + '\'' +
                '}';
    }
}
