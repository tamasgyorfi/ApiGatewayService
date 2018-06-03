package hu.bets.apigateway.model.schedules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Schedules {
    private final List<String> schedules;
    private final List<String> bets;
    private final Crests crests;
    private final List<String> errors;

    @JsonCreator
    public Schedules(@JsonProperty("schedules") List<String> schedules,
                     @JsonProperty("bets") List<String> bets,
                     @JsonProperty("crests") Crests crests,
                     @JsonProperty("errors") List<String> errors) {
        this.schedules = schedules;
        this.bets = bets;
        this.crests = crests;
        this.errors = errors;
    }
}
