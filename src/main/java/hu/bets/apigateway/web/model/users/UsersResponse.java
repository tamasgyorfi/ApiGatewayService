package hu.bets.apigateway.web.model.users;

public class UsersResponse<T> {
    private final T payload;
    private final String error;
    private final String token;

    private UsersResponse(T payload, String error, String token) {
        this.payload = payload;
        this.error = error;
        this.token = token;
    }

    public static <T> UsersResponse success(T payload, String token) {
        return new UsersResponse(payload, "", token);
    }

    public static <T> UsersResponse failure(String error, String token) {
        return new UsersResponse(null, error, token);
    }

}
