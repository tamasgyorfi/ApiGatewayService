package hu.bets.apigateway.model.users;

public class UserServiceErrorResponse {

    private final String payload = "";
    private final String error;
    private final String token;

    public UserServiceErrorResponse(String error, String token) {
        this.error = error;
        this.token = token;
    }
}
