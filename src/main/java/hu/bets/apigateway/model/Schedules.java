package hu.bets.apigateway.model;

import java.util.List;

public class Schedules {
    private final List<String> schedules;
    private final List<String> bets;
    private final Crests crests;

    public Schedules(List<String> schedules, List<String> bets, Crests crests) {
        this.schedules = schedules;
        this.bets = bets;
        this.crests = crests;
    }
}
