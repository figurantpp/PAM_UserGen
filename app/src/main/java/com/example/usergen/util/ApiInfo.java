package com.example.usergen.util;

public class ApiInfo {

    public static final String GENDER_QUERY_PARAMETER = "gender";
    public static final String NATIONALITY_QUERY_PARAMETER = "nat";
    public static final String INCLUDE_QUERY_PARAMETER =
            "id,name,email,gender,dob,picture,nat";

    public static final String API_URL = "https://randomuser.me/api";
    public static final String INCLUDE_QUERY_PARAMATER_LIST = "inc";

    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static final String[] NATIONALITY_ACRONYMS = new String[] {
            "AU", "BR", "CA", "CH", "DE", "DK", "ES", "FI",
            "FR", "GB", "IE", "IR", "NO", "NL", "NZ", "TR", "US"
    };
}
