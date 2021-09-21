package com.example.usergen.util;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApiInfo {

    public static final String GENDER_QUERY_PARAMETER = "gender";
    public static final String NATIONALITY_QUERY_PARAMETER = "nat";
    public static final String INCLUDE_QUERY_PARAMETER =
            "id,name,email,gender,dob,picture,nat";

    public static final String API_URL = "https://randomuser.me/api";
    public static final String INCLUDE_QUERY_PARAMATER_LIST = "inc";

    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static final String[] NATIONALITY_ACRONYMS = new String[]{
            "AU", "BR", "CA", "CH", "DE", "DK", "ES", "FI",
            "FR", "GB", "IE", "IR", "NO", "NL", "NZ", "TR", "US"
    };

    public static final String API_SAMPLE_USERNAME = "bob";
    public static final String API_SAMPLE_PASSWORD = "123";

    public static final Map<String, String> NATIONALITY_NAMES = Stream.of(
            new String[][]{
                    {"AU", "Australia"},
                    {"BR", "Brazil"},
                    {"CA", "Canada"},
                    {"CH", "Switzerland"},
                    {"DE", "Germany"},
                    {"DK", "Denmark"},
                    {"ES", "Spain"},
                    {"FI", "Finland"},
                    {"FR", "France"},
                    {"GB", "United Kingdom"},
                    {"IE", "Ireland"},
                    {"IR", "Iran"},
                    {"NO", "Norway"},
                    {"NL", "Netherlands"},
                    {"NZ", "New Zealand"},
                    {"TR", "Turkey"},
                    {"US", "United States"},
            }
    ).collect(Collectors.toMap(data -> data[0], data -> data[1]));

}
