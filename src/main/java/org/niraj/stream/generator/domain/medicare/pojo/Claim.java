package org.niraj.stream.generator.domain.medicare.pojo;

import com.opencsv.bean.CsvBindByName;
import org.niraj.stream.generator.configuration.ConfigReader;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Claim {

    public static final String CLAIM_CODE_INDEX = "claim-code";

    /**
     * Claim ID
     */
    @CsvBindByName(required = true, column = "CLM_ID")
    private String id;

    /**
     * Beneficiary Id
     */
    @CsvBindByName(required = true, column = "DESYNPUF_ID")
    private String patientId;

    /**
     * Claim Start Date
     */
    @CsvBindByName(required = true, column = "CLM_FROM_DT")
    private String startDate;

    /**
     * Claim end date
     */
    @CsvBindByName(required = true, column = "CLM_THRU_DT")
    private String endDate;

    /**
     * Claim Diagnosis Code 1-8
     */
    @CsvBindByName(column = "ICD9_DGNS_CD_1")
    private String claimDiagnosisCode1;

    @CsvBindByName(column = "ICD9_DGNS_CD_2")
    private String claimDiagnosisCode2;

    @CsvBindByName(column = "ICD9_DGNS_CD_3")
    private String claimDiagnosisCode3;

    @CsvBindByName(column = "ICD9_DGNS_CD_4")
    private String claimDiagnosisCode4;

    @CsvBindByName(column = "ICD9_DGNS_CD_5")
    private String claimDiagnosisCode5;

    @CsvBindByName(column = "ICD9_DGNS_CD_6")
    private String claimDiagnosisCode6;

    @CsvBindByName(column = "ICD9_DGNS_CD_7")
    private String claimDiagnosisCode7;

    @CsvBindByName(column = "ICD9_DGNS_CD_8")
    private String claimDiagnosisCode8;


    /**
     * Provider Physician National PIN 1-13
     */
    @CsvBindByName(column = "PRF_PHYSN_NPI_1")
    private String physician1;

    @CsvBindByName(column = "PRF_PHYSN_NPI_2")
    private String physician2;

    @CsvBindByName(column = "PRF_PHYSN_NPI_3")
    private String physician3;

    @CsvBindByName(column = "PRF_PHYSN_NPI_4")
    private String physician4;

    @CsvBindByName(column = "PRF_PHYSN_NPI_5")
    private String physician5;

    @CsvBindByName(column = "PRF_PHYSN_NPI_6")
    private String physician6;

    @CsvBindByName(column = "PRF_PHYSN_NPI_7")
    private String physician7;

    @CsvBindByName(column = "PRF_PHYSN_NPI_8")
    private String physician8;

    @CsvBindByName(column = "PRF_PHYSN_NPI_9")
    private String physician9;

    @CsvBindByName(column = "PRF_PHYSN_NPI_10")
    private String physician10;

    @CsvBindByName(column = "PRF_PHYSN_NPI_11")
    private String physician11;

    @CsvBindByName(column = "PRF_PHYSN_NPI_12")
    private String physician12;

    @CsvBindByName(column = "PRF_PHYSN_NPI_13")
    private String physician13;


    /**
     * Line HCFA Common Prcoedure Coding System 1-13
     */
    @CsvBindByName(column = "HCPCS_CD_1")
    private String HCPCSCode1;

    @CsvBindByName(column = "HCPCS_CD_2")
    private String HCPCSCode2;

    @CsvBindByName(column = "HCPCS_CD_3")
    private String HCPCSCode3;

    @CsvBindByName(column = "HCPCS_CD_4")
    private String HCPCSCode4;

    @CsvBindByName(column = "HCPCS_CD_5")
    private String HCPCSCode5;

    @CsvBindByName(column = "HCPCS_CD_6")
    private String HCPCSCode6;

    @CsvBindByName(column = "HCPCS_CD_7")
    private String HCPCSCode7;

    @CsvBindByName(column = "HCPCS_CD_8")
    private String HCPCSCode8;

    @CsvBindByName(column = "HCPCS_CD_9")
    private String HCPCSCode9;

    @CsvBindByName(column = "HCPCS_CD_10")
    private String HCPCSCode10;

    @CsvBindByName(column = "HCPCS_CD_11")
    private String HCPCSCode11;

    @CsvBindByName(column = "HCPCS_CD_12")
    private String HCPCSCode12;

    @CsvBindByName(column = "HCPCS_CD_13")
    private String HCPCSCode13;

    /**
     * Line Processing Indication Code
     */
    @CsvBindByName(column = "LINE_PRCSG_IND_CD_1")
    private String lineProcessingIndicationCode1;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_2")
    private String lineProcessingIndicationCode2;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_3")
    private String lineProcessingIndicationCode3;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_4")
    private String lineProcessingIndicationCode4;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_5")
    private String lineProcessingIndicationCode5;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_6")
    private String lineProcessingIndicationCode6;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_7")
    private String lineProcessingIndicationCode7;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_8")
    private String lineProcessingIndicationCode8;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_9")
    private String lineProcessingIndicationCode9;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_10")
    private String lineProcessingIndicationCode10;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_11")
    private String lineProcessingIndicationCode11;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_12")
    private String lineProcessingIndicationCode12;

    @CsvBindByName(column = "LINE_PRCSG_IND_CD_13")
    private String lineProcessingIndicationCode13;


    /**
     * Line Diagnosis Code 1-13
     */
    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_1")
    private String lineDiagnosisCode1;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_2")
    private String lineDiagnosisCode2;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_3")
    private String lineDiagnosisCode3;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_4")
    private String lineDiagnosisCode4;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_5")
    private String lineDiagnosisCode5;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_6")
    private String lineDiagnosisCode6;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_7")
    private String lineDiagnosisCode7;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_8")
    private String lineDiagnosisCode8;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_9")
    private String lineDiagnosisCode9;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_10")
    private String lineDiagnosisCode10;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_11")
    private String lineDiagnosisCode11;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_12")
    private String lineDiagnosisCode12;

    @CsvBindByName(column = "LINE_ICD9_DGNS_CD_13")
    private String lineDiagnosisCode13;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getStartDate() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
        try {
            return new SimpleDateFormat("MM-dd-yyyy").format(simpleDate.parse(this.startDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
        try {
            return new SimpleDateFormat("MM-dd-yyyy").format(simpleDate.parse(this.endDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getClaimDiagnosisCode1() {
        return claimDiagnosisCode1;
    }

    public void setClaimDiagnosisCode1(String claimDiagnosisCode1) {
        this.claimDiagnosisCode1 = claimDiagnosisCode1;
    }

    public String getClaimDiagnosisCode2() {
        return claimDiagnosisCode2;
    }

    public void setClaimDiagnosisCode2(String claimDiagnosisCode2) {
        this.claimDiagnosisCode2 = claimDiagnosisCode2;
    }

    public String getClaimDiagnosisCode3() {
        return claimDiagnosisCode3;
    }

    public void setClaimDiagnosisCode3(String claimDiagnosisCode3) {
        this.claimDiagnosisCode3 = claimDiagnosisCode3;
    }

    public String getClaimDiagnosisCode4() {
        return claimDiagnosisCode4;
    }

    public void setClaimDiagnosisCode4(String claimDiagnosisCode4) {
        this.claimDiagnosisCode4 = claimDiagnosisCode4;
    }

    public String getClaimDiagnosisCode5() {
        return claimDiagnosisCode5;
    }

    public void setClaimDiagnosisCode5(String claimDiagnosisCode5) {
        this.claimDiagnosisCode5 = claimDiagnosisCode5;
    }

    public String getClaimDiagnosisCode6() {
        return claimDiagnosisCode6;
    }

    public void setClaimDiagnosisCode6(String claimDiagnosisCode6) {
        this.claimDiagnosisCode6 = claimDiagnosisCode6;
    }

    public String getClaimDiagnosisCode7() {
        return claimDiagnosisCode7;
    }

    public void setClaimDiagnosisCode7(String claimDiagnosisCode7) {
        this.claimDiagnosisCode7 = claimDiagnosisCode7;
    }

    public String getClaimDiagnosisCode8() {
        return claimDiagnosisCode8;
    }

    public void setClaimDiagnosisCode8(String claimDiagnosisCode8) {
        this.claimDiagnosisCode8 = claimDiagnosisCode8;
    }

    public String getPhysician1() {
        return physician1;
    }

    public void setPhysician1(String physician1) {
        this.physician1 = physician1;
    }

    public String getPhysician2() {
        return physician2;
    }

    public void setPhysician2(String physician2) {
        this.physician2 = physician2;
    }

    public String getPhysician3() {
        return physician3;
    }

    public void setPhysician3(String physician3) {
        this.physician3 = physician3;
    }

    public String getPhysician4() {
        return physician4;
    }

    public void setPhysician4(String physician4) {
        this.physician4 = physician4;
    }

    public String getPhysician5() {
        return physician5;
    }

    public void setPhysician5(String physician5) {
        this.physician5 = physician5;
    }

    public String getPhysician6() {
        return physician6;
    }

    public void setPhysician6(String physician6) {
        this.physician6 = physician6;
    }

    public String getPhysician7() {
        return physician7;
    }

    public void setPhysician7(String physician7) {
        this.physician7 = physician7;
    }

    public String getPhysician8() {
        return physician8;
    }

    public void setPhysician8(String physician8) {
        this.physician8 = physician8;
    }

    public String getPhysician9() {
        return physician9;
    }

    public void setPhysician9(String physician9) {
        this.physician9 = physician9;
    }

    public String getPhysician10() {
        return physician10;
    }

    public void setPhysician10(String physician10) {
        this.physician10 = physician10;
    }

    public String getPhysician11() {
        return physician11;
    }

    public void setPhysician11(String physician11) {
        this.physician11 = physician11;
    }

    public String getPhysician12() {
        return physician12;
    }

    public void setPhysician12(String physician12) {
        this.physician12 = physician12;
    }

    public String getPhysician13() {
        return physician13;
    }

    public void setPhysician13(String physician13) {
        this.physician13 = physician13;
    }

    public String getHCPCSCode1() {
        return HCPCSCode1;
    }

    public void setHCPCSCode1(String HCPCSCode1) {
        this.HCPCSCode1 = HCPCSCode1;
    }

    public String getHCPCSCode2() {
        return HCPCSCode2;
    }

    public void setHCPCSCode2(String HCPCSCode2) {
        this.HCPCSCode2 = HCPCSCode2;
    }

    public String getHCPCSCode3() {
        return HCPCSCode3;
    }

    public void setHCPCSCode3(String HCPCSCode3) {
        this.HCPCSCode3 = HCPCSCode3;
    }

    public String getHCPCSCode4() {
        return HCPCSCode4;
    }

    public void setHCPCSCode4(String HCPCSCode4) {
        this.HCPCSCode4 = HCPCSCode4;
    }

    public String getHCPCSCode5() {
        return HCPCSCode5;
    }

    public void setHCPCSCode5(String HCPCSCode5) {
        this.HCPCSCode5 = HCPCSCode5;
    }

    public String getHCPCSCode6() {
        return HCPCSCode6;
    }

    public void setHCPCSCode6(String HCPCSCode6) {
        this.HCPCSCode6 = HCPCSCode6;
    }

    public String getHCPCSCode7() {
        return HCPCSCode7;
    }

    public void setHCPCSCode7(String HCPCSCode7) {
        this.HCPCSCode7 = HCPCSCode7;
    }

    public String getHCPCSCode8() {
        return HCPCSCode8;
    }

    public void setHCPCSCode8(String HCPCSCode8) {
        this.HCPCSCode8 = HCPCSCode8;
    }

    public String getHCPCSCode9() {
        return HCPCSCode9;
    }

    public void setHCPCSCode9(String HCPCSCode9) {
        this.HCPCSCode9 = HCPCSCode9;
    }

    public String getHCPCSCode10() {
        return HCPCSCode10;
    }

    public void setHCPCSCode10(String HCPCSCode10) {
        this.HCPCSCode10 = HCPCSCode10;
    }

    public String getHCPCSCode11() {
        return HCPCSCode11;
    }

    public void setHCPCSCode11(String HCPCSCode11) {
        this.HCPCSCode11 = HCPCSCode11;
    }

    public String getHCPCSCode12() {
        return HCPCSCode12;
    }

    public void setHCPCSCode12(String HCPCSCode12) {
        this.HCPCSCode12 = HCPCSCode12;
    }

    public String getHCPCSCode13() {
        return HCPCSCode13;
    }

    public void setHCPCSCode13(String HCPCSCode13) {
        this.HCPCSCode13 = HCPCSCode13;
    }

    public String getLineProcessingIndicationCode1() {
        return lineProcessingIndicationCode1;
    }

    public void setLineProcessingIndicationCode1(String lineProcessingIndicationCode1) {
        this.lineProcessingIndicationCode1 = lineProcessingIndicationCode1;
    }

    public String getLineProcessingIndicationCode2() {
        return lineProcessingIndicationCode2;
    }

    public void setLineProcessingIndicationCode2(String lineProcessingIndicationCode2) {
        this.lineProcessingIndicationCode2 = lineProcessingIndicationCode2;
    }

    public String getLineProcessingIndicationCode3() {
        return lineProcessingIndicationCode3;
    }

    public void setLineProcessingIndicationCode3(String lineProcessingIndicationCode3) {
        this.lineProcessingIndicationCode3 = lineProcessingIndicationCode3;
    }

    public String getLineProcessingIndicationCode4() {
        return lineProcessingIndicationCode4;
    }

    public void setLineProcessingIndicationCode4(String lineProcessingIndicationCode4) {
        this.lineProcessingIndicationCode4 = lineProcessingIndicationCode4;
    }

    public String getLineProcessingIndicationCode5() {
        return lineProcessingIndicationCode5;
    }

    public void setLineProcessingIndicationCode5(String lineProcessingIndicationCode5) {
        this.lineProcessingIndicationCode5 = lineProcessingIndicationCode5;
    }

    public String getLineProcessingIndicationCode6() {
        return lineProcessingIndicationCode6;
    }

    public void setLineProcessingIndicationCode6(String lineProcessingIndicationCode6) {
        this.lineProcessingIndicationCode6 = lineProcessingIndicationCode6;
    }

    public String getLineProcessingIndicationCode7() {
        return lineProcessingIndicationCode7;
    }

    public void setLineProcessingIndicationCode7(String lineProcessingIndicationCode7) {
        this.lineProcessingIndicationCode7 = lineProcessingIndicationCode7;
    }

    public String getLineProcessingIndicationCode8() {
        return lineProcessingIndicationCode8;
    }

    public void setLineProcessingIndicationCode8(String lineProcessingIndicationCode8) {
        this.lineProcessingIndicationCode8 = lineProcessingIndicationCode8;
    }

    public String getLineProcessingIndicationCode9() {
        return lineProcessingIndicationCode9;
    }

    public void setLineProcessingIndicationCode9(String lineProcessingIndicationCode9) {
        this.lineProcessingIndicationCode9 = lineProcessingIndicationCode9;
    }

    public String getLineProcessingIndicationCode10() {
        return lineProcessingIndicationCode10;
    }

    public void setLineProcessingIndicationCode10(String lineProcessingIndicationCode10) {
        this.lineProcessingIndicationCode10 = lineProcessingIndicationCode10;
    }

    public String getLineProcessingIndicationCode11() {
        return lineProcessingIndicationCode11;
    }

    public void setLineProcessingIndicationCode11(String lineProcessingIndicationCode11) {
        this.lineProcessingIndicationCode11 = lineProcessingIndicationCode11;
    }

    public String getLineProcessingIndicationCode12() {
        return lineProcessingIndicationCode12;
    }

    public void setLineProcessingIndicationCode12(String lineProcessingIndicationCode12) {
        this.lineProcessingIndicationCode12 = lineProcessingIndicationCode12;
    }

    public String getLineProcessingIndicationCode13() {
        return lineProcessingIndicationCode13;
    }

    public void setLineProcessingIndicationCode13(String lineProcessingIndicationCode13) {
        this.lineProcessingIndicationCode13 = lineProcessingIndicationCode13;
    }

    public String getLineDiagnosisCode1() {
        return lineDiagnosisCode1;
    }

    public void setLineDiagnosisCode1(String lineDiagnosisCode1) {
        this.lineDiagnosisCode1 = lineDiagnosisCode1;
    }

    public String getLineDiagnosisCode2() {
        return lineDiagnosisCode2;
    }

    public void setLineDiagnosisCode2(String lineDiagnosisCode2) {
        this.lineDiagnosisCode2 = lineDiagnosisCode2;
    }

    public String getLineDiagnosisCode3() {
        return lineDiagnosisCode3;
    }

    public void setLineDiagnosisCode3(String lineDiagnosisCode3) {
        this.lineDiagnosisCode3 = lineDiagnosisCode3;
    }

    public String getLineDiagnosisCode4() {
        return lineDiagnosisCode4;
    }

    public void setLineDiagnosisCode4(String lineDiagnosisCode4) {
        this.lineDiagnosisCode4 = lineDiagnosisCode4;
    }

    public String getLineDiagnosisCode5() {
        return lineDiagnosisCode5;
    }

    public void setLineDiagnosisCode5(String lineDiagnosisCode5) {
        this.lineDiagnosisCode5 = lineDiagnosisCode5;
    }

    public String getLineDiagnosisCode6() {
        return lineDiagnosisCode6;
    }

    public void setLineDiagnosisCode6(String lineDiagnosisCode6) {
        this.lineDiagnosisCode6 = lineDiagnosisCode6;
    }

    public String getLineDiagnosisCode7() {
        return lineDiagnosisCode7;
    }

    public void setLineDiagnosisCode7(String lineDiagnosisCode7) {
        this.lineDiagnosisCode7 = lineDiagnosisCode7;
    }

    public String getLineDiagnosisCode8() {
        return lineDiagnosisCode8;
    }

    public void setLineDiagnosisCode8(String lineDiagnosisCode8) {
        this.lineDiagnosisCode8 = lineDiagnosisCode8;
    }

    public String getLineDiagnosisCode9() {
        return lineDiagnosisCode9;
    }

    public void setLineDiagnosisCode9(String lineDiagnosisCode9) {
        this.lineDiagnosisCode9 = lineDiagnosisCode9;
    }

    public String getLineDiagnosisCode10() {
        return lineDiagnosisCode10;
    }

    public void setLineDiagnosisCode10(String lineDiagnosisCode10) {
        this.lineDiagnosisCode10 = lineDiagnosisCode10;
    }

    public String getLineDiagnosisCode11() {
        return lineDiagnosisCode11;
    }

    public void setLineDiagnosisCode11(String lineDiagnosisCode11) {
        this.lineDiagnosisCode11 = lineDiagnosisCode11;
    }

    public String getLineDiagnosisCode12() {
        return lineDiagnosisCode12;
    }

    public void setLineDiagnosisCode12(String lineDiagnosisCode12) {
        this.lineDiagnosisCode12 = lineDiagnosisCode12;
    }

    public String getLineDiagnosisCode13() {
        return lineDiagnosisCode13;
    }

    public void setLineDiagnosisCode13(String lineDiagnosisCode13) {
        this.lineDiagnosisCode13 = lineDiagnosisCode13;
    }


    public Map<Integer, String> getPhysicianList() {
        return this.getAttributeValues("physician");
    }

    public Map<Integer, String> getClaimDiagnosisCodeList() {
        return this.getAttributeValues("claimDiagnosisCode");
    }

    public Map<Integer, String> getHCPCSCodeList() {
        return this.getAttributeValues("HCPCSCode");
    }

    public Map<Integer, String> getLineProcessingIndicationCodeList() {
        return this.getAttributeValues("lineProcessingIndicationCode");
    }

    public Map<Integer, String> getLineDiagnosisCodeList() {
        return this.getAttributeValues("lineDiagnosisCode");
    }

    public boolean claimCodeExists() {
        return (this.getClaimDiagnosisCodeList().size() > 0);
    }

    @Override
    public String toString() {
        return "Beneficiary Id: " + patientId + ", Claim Id: " + id;
    }


    private Map<Integer, String> getAttributeValues(String attributeStartsWith) {
        List<Field> fields = Arrays.asList(getClass().getDeclaredFields()).stream()
                .filter(e -> e.getName().startsWith(attributeStartsWith))
                .collect(Collectors.toList());

        Map<Integer, Field> attributes = new HashMap<>();
        int count = 1;
        for (Field field : fields) {
            field.setAccessible(true);
            attributes.put(count, field);
            count++;
        }

        return attributes.entrySet().stream()
                .filter(e-> filterAttributes(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> getAttributeValue(e.getValue())));
    }

    private boolean filterAttributes(Field field) {
        try {
            String statusCode = ConfigReader.getInstance().getProperty(CLAIM_CODE_INDEX);
            return (field.get(this) != null)
                    && field.get(this).toString().startsWith(statusCode);

        } catch (IllegalAccessException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    private String getAttributeValue(Field field) {
        try {
            return (field.get(this) != null) ? field.get(this).toString() : null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
