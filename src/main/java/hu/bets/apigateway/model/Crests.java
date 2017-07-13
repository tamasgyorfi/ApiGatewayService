package hu.bets.apigateway.model;

import java.util.Map;

public class Crests {

    private final Map<String, String> crests;
    private final String baseUrl;
    private final String format;

    public Crests(Map<String, String> crests, String baseUrl, String format) {

        this.crests = crests;
        this.baseUrl = baseUrl;
        this.format = format;
    }
}
