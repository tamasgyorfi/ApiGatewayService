package hu.bets.apigateway.web.model;

public class SchedulesRequest {
    private String userId;

    public SchedulesRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
