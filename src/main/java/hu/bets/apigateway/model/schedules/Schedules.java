package hu.bets.apigateway.model.schedules;

import java.util.List;

public class Schedules {
    private final List<String> schedules;
    private final List<String> bets;
    private final Crests crests;
    private final List<String> errors;

    public Schedules(List<String> schedules, List<String> bets, Crests crests, List<String> errors) {
        this.schedules = schedules;
        this.bets = bets;
        this.crests = crests;
        this.errors = errors;
    }
}
