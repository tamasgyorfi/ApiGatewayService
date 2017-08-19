package hu.bets.apigateway.model.bets;

import java.util.Collections;
import java.util.List;

/**
 * Meant to be JSONified only. No need for getters/setters.
 */
@SuppressWarnings("unused")
public class BetServiceErrorResponse {
    private final List<Bet> payload = Collections.emptyList();
    private String error;
    private String token;

    public BetServiceErrorResponse(String error, String token) {
        this.error = error;
        this.token = token;
    }
}
