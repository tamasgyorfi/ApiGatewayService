package hu.bets.apigateway.model.schedules;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
