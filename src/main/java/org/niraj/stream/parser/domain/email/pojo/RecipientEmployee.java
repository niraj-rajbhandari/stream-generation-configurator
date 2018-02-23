package org.niraj.stream.parser.domain.email.pojo;

public class RecipientEmployee extends Employee {

    private String receptionMethod;

    public String getReceptionMethod() {
        return receptionMethod;
    }

    public void setReceptionMethod(String receptionMethod) {
        this.receptionMethod = receptionMethod;
    }

    @Override
    public String toString() {
        return "RecipientEmployee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
