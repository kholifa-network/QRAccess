package com.kholifa.qraccess.countrylist;

public class Country_app {
    private final String iso;
    private final String phone_code;
    private final String name;

    public Country_app(String iso, String phone_code, String name) {
        this.iso = iso;
        this.phone_code = phone_code;
        this.name = name;
    }

    public String getIso() {
        return iso;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getName() {
        return name;
    }


    boolean isEligibleForQuery(String query) {
        query = query.toLowerCase();
        return getName().toLowerCase().contains(query)
                || getIso().toLowerCase().contains(query)
                || getPhone_code().toLowerCase().contains(query);
    }
}
