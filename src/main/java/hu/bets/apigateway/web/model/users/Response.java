package hu.bets.apigateway.web.model.users;

public class Response<T> {
    private final T payload;
    private final String error;
    private final String token;

    private Response(T payload, String error, String token) {
        this.payload = payload;
        this.error = error;
        this.token = token;
    }

    public static <T> Response success(T payload, String token) {
        return new Response(payload, "", token);
    }

    public static <T> Response failure(String error, String token) {
        return new Response(null, error, token);
    }

}
