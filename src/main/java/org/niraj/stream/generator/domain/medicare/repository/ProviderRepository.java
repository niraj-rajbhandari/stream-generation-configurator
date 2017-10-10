package org.niraj.stream.generator.domain.medicare.repository;

import org.niraj.stream.generator.domain.medicare.pojo.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProviderRepository {

    private List<Provider> providerList = null;
    private static ProviderRepository ourInstance = new ProviderRepository();

    public static ProviderRepository getInstance() {
        return ourInstance;
    }

    private ProviderRepository() {
        this.setProviderList();
    }

    public void setProviderList() {
        if (providerList == null) {
            providerList = new ArrayList<>();

            providerList.add(new Provider("0001", "0001", "0879"));
            providerList.add(new Provider("0880", "0880", "0899"));
            providerList.add(new Provider("0900", "0900", "0999"));
            providerList.add(new Provider("1000", "1000", "1199"));
            providerList.add(new Provider("1200", "1200", "1224"));
            providerList.add(new Provider("1225", "1225", "1299"));
            providerList.add(new Provider("1300", "1300", "1399"));
            providerList.add(new Provider("1400", "1400", "1499"));
            providerList.add(new Provider("1500", "1500", "1799"));
            providerList.add(new Provider("1800", "1800", "1989"));
            providerList.add(new Provider("1990", "1990", "1999"));
            providerList.add(new Provider("2000", "2000", "2299"));
            providerList.add(new Provider("2300", "2300", "2499"));
            providerList.add(new Provider("2500", "2500", "2899"));
            providerList.add(new Provider("2900", "2900", "2999"));
            providerList.add(new Provider("3000", "3000", "3024"));
            providerList.add(new Provider("3025", "3025", "3099"));
            providerList.add(new Provider("3100", "3100", "3199"));
            providerList.add(new Provider("3200", "3200", "3299"));
            providerList.add(new Provider("3300", "3300", "3399"));
            providerList.add(new Provider("3400", "3400", "3499"));
            providerList.add(new Provider("3500", "3500", "3699"));
            providerList.add(new Provider("3700", "3700", "3799"));
            providerList.add(new Provider("3800", "3800", "3974"));
            providerList.add(new Provider("3975", "3975", "3999"));
            providerList.add(new Provider("4000", "4000", "4499"));
            providerList.add(new Provider("4500", "4500", "4599"));
            providerList.add(new Provider("4600", "4600", "4799"));
            providerList.add(new Provider("4800", "4800", "4899"));
            providerList.add(new Provider("4900", "4900", "4999"));
            providerList.add(new Provider("5000", "5000", "6499"));
            providerList.add(new Provider("6500", "6500", "6989"));
            providerList.add(new Provider("6990", "6990", "6999"));
            providerList.add(new Provider("7000", "7000", "7299"));
            providerList.add(new Provider("7300", "7300", "7399"));
            providerList.add(new Provider("7400", "7400", "7799"));
            providerList.add(new Provider("7800", "7800", "7999"));
            providerList.add(new Provider("8000", "8000", "8499"));
            providerList.add(new Provider("8500", "8500", "8899"));
            providerList.add(new Provider("8900", "8900", "8999"));
            providerList.add(new Provider("9000", "9000", "9799"));
            providerList.add(new Provider("9800", "9800", "9899"));
            providerList.add(new Provider("9900", "9900", "9999"));
            providerList.add(new Provider("4900", "4900", "4999"));
        }
    }

    public List<Provider> getProviderList() {
        return providerList;
    }

    public String find(String providerCode) {
        List<Provider> filteredList = providerList.stream()
                .filter(p -> Integer.parseInt(p.getLowPhysicianId()) <= Integer.parseInt(providerCode)
                        && Integer.parseInt(p.getHighPhysicianId()) >= Integer.parseInt(providerCode))
                .collect(Collectors.toList());

        return (filteredList.size() > 0) ? filteredList.get(0).getInstitute() : "Other";
    }
}
