package hu.bets.apigateway.model.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FriendsUpdate {

    private final String originatingUserId;
    private final List<String> friendsTracked;
    private final List<String> friendsUntracked;

    private String token;

    @JsonCreator
    public FriendsUpdate(@JsonProperty("originatingUserId") String originatingUserId,
                         @JsonProperty("friendsTracked") List<String> friendsTracked,
                         @JsonProperty("friendsUntracked") List<String> friendsUntracked) {
        this.originatingUserId = originatingUserId;
        this.friendsTracked = friendsTracked;
        this.friendsUntracked = friendsUntracked;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
