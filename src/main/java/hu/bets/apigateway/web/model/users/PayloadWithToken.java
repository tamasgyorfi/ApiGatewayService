package hu.bets.apigateway.web.model.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import hu.bets.apigateway.model.users.User;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayloadWithToken {
    private List<User> payload;
    private String error;
    private String token;

    @JsonCreator
    public PayloadWithToken(@JsonProperty("payload") List<User> payload,
                            @JsonProperty("error") String error,
                            @JsonProperty("token") String token) {
        this.payload = payload;
        this.error = error;
        this.token = token;
    }

    public List<User> getPayload() {
        return Collections.unmodifiableList(payload);
    }

    public String getToken() {
        return token;
    }

    public String getError() {
        return error;
    }
}
