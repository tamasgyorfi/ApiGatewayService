package hu.bets.apigateway.model;

import java.util.Collections;
import java.util.List;

/**
 * For JSON conversion only.
 */
@SuppressWarnings("unused")
public class ScheduleServiceErrorResponse {
    private String error;
    private List<?> matches = Collections.emptyList();
    private String token;

    public ScheduleServiceErrorResponse(String error, String token) {
        this.error = error;
        this.token = token;
    }
}
