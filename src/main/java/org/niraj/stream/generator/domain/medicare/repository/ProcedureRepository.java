package org.niraj.stream.generator.domain.medicare.repository;

import org.niraj.stream.generator.domain.medicare.pojo.Procedure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcedureRepository {
    private static ProcedureRepository ourInstance = new ProcedureRepository();

    private List<Procedure> procedureList = null;

    public static ProcedureRepository getInstance() {
        return ourInstance;
    }

    private ProcedureRepository() {
        this.setProcedureList();
    }

    public void setProcedureList() {
        if (procedureList == null) {
            procedureList = new ArrayList<>();

            //Evaluation and Management
            procedureList.add(new Procedure("99201", "99201", "99215", "Evaluation and Management"));
            procedureList.add(new Procedure("99217", "99217", "99220", "Evaluation and Management"));
            procedureList.add(new Procedure("99221", "99221", "99239", "Evaluation and Management"));
            procedureList.add(new Procedure("99241", "99241", "99255", "Evaluation and Management"));
            procedureList.add(new Procedure("99281", "99281", "99288", "Evaluation and Management"));
            procedureList.add(new Procedure("99291", "99291", "99292", "Evaluation and Management"));
            procedureList.add(new Procedure("99304", "99304", "99318", "Evaluation and Management"));
            procedureList.add(new Procedure("99324", "99324", "99337", "Evaluation and Management"));
            procedureList.add(new Procedure("99339", "99339", "99340", "Evaluation and Management"));
            procedureList.add(new Procedure("99341", "99341", "99350", "Evaluation and Management"));
            procedureList.add(new Procedure("99354", "99354", "99360", "Evaluation and Management"));
            procedureList.add(new Procedure("99363", "99363", "99368", "Evaluation and Management"));
            procedureList.add(new Procedure("99374", "99374", "99380", "Evaluation and Management"));
            procedureList.add(new Procedure("99381", "99381", "99429", "Evaluation and Management"));
            procedureList.add(new Procedure("99441", "99441", "99444", "Evaluation and Management"));
            procedureList.add(new Procedure("99450", "99450", "99456", "Evaluation and Management"));
            procedureList.add(new Procedure("99460", "99460", "99465", "Evaluation and Management"));
            procedureList.add(new Procedure("99466", "99466", "99480", "Evaluation and Management"));
            procedureList.add(new Procedure("99487", "99487", "99489", "Evaluation and Management"));
            procedureList.add(new Procedure("99495", "99495", "99496", "Evaluation and Management"));
            procedureList.add(new Procedure("99499", "99499", "99499", "Evaluation and Management"));

            //Anesthesia
            procedureList.add(new Procedure("00100", "00100", "00222", "Anesthesia"));
            procedureList.add(new Procedure("00300", "00300", "00352", "Anesthesia"));
            procedureList.add(new Procedure("00400", "00400", "00474", "Anesthesia"));
            procedureList.add(new Procedure("00500", "00500", "00580", "Anesthesia"));
            procedureList.add(new Procedure("00600", "00600", "00670", "Anesthesia"));
            procedureList.add(new Procedure("00700", "00700", "00797", "Anesthesia"));
            procedureList.add(new Procedure("00800", "00800", "00882", "Anesthesia"));
            procedureList.add(new Procedure("00902", "00902", "00952", "Anesthesia"));
            procedureList.add(new Procedure("01112", "01112", "01190", "Anesthesia"));
            procedureList.add(new Procedure("01200", "01200", "01274", "Anesthesia"));
            procedureList.add(new Procedure("01320", "01320", "01444", "Anesthesia"));
            procedureList.add(new Procedure("01462", "01462", "01522", "Anesthesia"));
            procedureList.add(new Procedure("01610", "01610", "01682", "Anesthesia"));
            procedureList.add(new Procedure("01710", "01710", "01782", "Anesthesia"));
            procedureList.add(new Procedure("01810", "01810", "01860", "Anesthesia"));
            procedureList.add(new Procedure("01916", "01916", "01936", "Anesthesia"));
            procedureList.add(new Procedure("01951", "01951", "01953", "Anesthesia"));
            procedureList.add(new Procedure("01958", "01958", "01969", "Anesthesia"));
            procedureList.add(new Procedure("01990", "01990", "01999", "Anesthesia"));
            procedureList.add(new Procedure("99100", "99100", "99140", "Anesthesia"));
            procedureList.add(new Procedure("99143", "99143", "99150", "Anesthesia"));

            //Surgery
            procedureList.add(new Procedure("10000", "10000", "10022", "Surgery"));
            procedureList.add(new Procedure("10040", "10040", "19499", "Surgery"));
            procedureList.add(new Procedure("20000", "20000", "29999", "Surgery"));
            procedureList.add(new Procedure("30000", "30000", "32999", "Surgery"));
            procedureList.add(new Procedure("33010", "33010", "37799", "Surgery"));
            procedureList.add(new Procedure("38100", "38100", "38999", "Surgery"));
            procedureList.add(new Procedure("39000", "39000", "39599", "Surgery"));
            procedureList.add(new Procedure("40490", "40490", "49999", "Surgery"));
            procedureList.add(new Procedure("50010", "50010", "53899", "Surgery"));
            procedureList.add(new Procedure("54000", "54000", "55899", "Surgery"));
            procedureList.add(new Procedure("55920", "55920", "55980", "Surgery"));
            procedureList.add(new Procedure("56405", "56405", "58999", "Surgery"));
            procedureList.add(new Procedure("59000", "59000", "59899", "Surgery"));
            procedureList.add(new Procedure("60000", "60000", "60699", "Surgery"));
            procedureList.add(new Procedure("61000", "61000", "64999", "Surgery"));
            procedureList.add(new Procedure("65091", "65091", "68899", "Surgery"));
            procedureList.add(new Procedure("69000", "69000", "69979", "Surgery"));

            //Radiology
            procedureList.add(new Procedure("76500", "76500", "76999", "Radiology"));
            procedureList.add(new Procedure("77001", "77001", "77032", "Radiology"));
            procedureList.add(new Procedure("77051", "77051", "77059", "Radiology"));
            procedureList.add(new Procedure("77071", "77071", "77084", "Radiology"));
            procedureList.add(new Procedure("77261", "77261", "77999", "Radiology"));
            procedureList.add(new Procedure("78000", "78000", "79999", "Radiology"));

            //Pathology
            procedureList.add(new Procedure("80000", "80000", "80076", "Pathology"));
            procedureList.add(new Procedure("80100", "80100", "80103", "Pathology"));
            procedureList.add(new Procedure("80150", "80150", "80299", "Pathology"));
            procedureList.add(new Procedure("80400", "80400", "80444", "Pathology"));
            procedureList.add(new Procedure("80500", "80500", "80502", "Pathology"));
            procedureList.add(new Procedure("81000", "81000", "81099", "Pathology"));
            procedureList.add(new Procedure("82000", "82000", "84999", "Pathology"));
            procedureList.add(new Procedure("85002", "85002", "85999", "Pathology"));
            procedureList.add(new Procedure("86000", "86000", "86849", "Pathology"));
            procedureList.add(new Procedure("86850", "86850", "86999", "Pathology"));
            procedureList.add(new Procedure("87001", "87001", "87999", "Pathology"));
            procedureList.add(new Procedure("88000", "88000", "88099", "Pathology"));
            procedureList.add(new Procedure("88104", "88104", "88199", "Pathology"));
            procedureList.add(new Procedure("88230", "88230", "88299", "Pathology"));
            procedureList.add(new Procedure("88300", "88300", "88399", "Pathology"));
            procedureList.add(new Procedure("88720", "88720", "88741", "Pathology"));
            procedureList.add(new Procedure("89049", "89049", "89240", "Pathology"));
            procedureList.add(new Procedure("89250", "89250", "89398", "Pathology"));

            //Medicine
            procedureList.add(new Procedure("90281", "90281", "90399", "Medicine"));
            procedureList.add(new Procedure("90465", "90465", "90474", "Medicine"));
            procedureList.add(new Procedure("90801", "90801", "90899", "Medicine"));
            procedureList.add(new Procedure("90901", "90901", "90911", "Medicine"));
            procedureList.add(new Procedure("90935", "90935", "90999", "Medicine"));
            procedureList.add(new Procedure("91000", "91000", "91299", "Medicine"));
            procedureList.add(new Procedure("92002", "92002", "92499", "Medicine"));
            procedureList.add(new Procedure("92502", "92502", "92700", "Medicine"));
            procedureList.add(new Procedure("92950", "92950", "93799", "Medicine"));
            procedureList.add(new Procedure("93875", "93875", "93990", "Medicine"));
            procedureList.add(new Procedure("94002", "94002", "94799", "Medicine"));
            procedureList.add(new Procedure("95004", "95004", "95199", "Medicine"));
            procedureList.add(new Procedure("95250", "95250", "95251", "Medicine"));
            procedureList.add(new Procedure("95803", "95803", "96020", "Medicine"));
            procedureList.add(new Procedure("96101", "96101", "96125", "Medicine"));
            procedureList.add(new Procedure("96150", "96150", "96155", "Medicine"));
            procedureList.add(new Procedure("96360", "96360", "96549", "Medicine"));
            procedureList.add(new Procedure("96567", "96567", "96571", "Medicine"));
            procedureList.add(new Procedure("96900", "96900", "96999", "Medicine"));
            procedureList.add(new Procedure("97001", "97001", "97799", "Medicine"));
            procedureList.add(new Procedure("97802", "97802", "97804", "Medicine"));
            procedureList.add(new Procedure("97810", "97810", "97814", "Medicine"));
            procedureList.add(new Procedure("98925", "98925", "98929", "Medicine"));
            procedureList.add(new Procedure("98940", "98940", "98943", "Medicine"));
            procedureList.add(new Procedure("98960", "98960", "98962", "Medicine"));
            procedureList.add(new Procedure("98966", "98966", "98969", "Medicine"));
            procedureList.add(new Procedure("99000", "99000", "99091", "Medicine"));
            procedureList.add(new Procedure("99170", "99170", "99199", "Medicine"));
            procedureList.add(new Procedure("99500", "99500", "99602", "Medicine"));
            procedureList.add(new Procedure("99605", "99605", "99607", "Medicine"));
        }
    }

    public List<Procedure> getProcedureList() {
        return procedureList;
    }

    public String find(String procedureCode) {
        if (procedureCode.matches("[A-Za-z-0-9]+")) {
            if (Character.isAlphabetic(procedureCode.charAt(0))) {
                return Character.toString(procedureCode.charAt(0));
            } else if (Character.isAlphabetic(procedureCode.charAt(4))) {
                return Character.toString(procedureCode.charAt(4));
//            } else if (Character.isAlphabetic(procedureCode.charAt(3))) {
//                return "Other";
            } else {
                return "Other";
            }
        } else {
            List<Procedure> filteredList = procedureList.stream()
                    .filter(p -> Integer.parseInt(procedureCode) >= Integer.parseInt(p.getLowHCPCSCode())
                            && Integer.parseInt(procedureCode) <= Integer.parseInt(p.getHighHCPCSCode()))
                    .collect(Collectors.toList());

            return (filteredList.size() > 0) ? filteredList.get(0).getProcedure() : "Other";
        }
    }
}
