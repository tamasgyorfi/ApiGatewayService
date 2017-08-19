package hu.bets.apigateway.web.model.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetFriendsPayload {

    private String userId;
    private String token;

    @JsonCreator
    public GetFriendsPayload(@JsonProperty("userId") String userId, @JsonProperty("token") String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
