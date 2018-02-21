package org.niraj.stream.parser.enumerator;

public enum EmailStateTypeEnum {
    ORIGINAL("original"),
    FORWARDED("forwarded"),
    REPLIED("replied");

    private String emailStateType;

    private EmailStateTypeEnum(String stateType) {
        emailStateType = stateType;
    }

    public String getValue() {
        return emailStateType;
    }

}
