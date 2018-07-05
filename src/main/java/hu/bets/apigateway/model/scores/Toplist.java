package hu.bets.apigateway.model.scores;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import hu.bets.apigateway.model.users.User;

import java.util.List;

public class Toplist {
    private List<User> users;
    private List<ToplistEntry> scores;

    @JsonCreator
    public Toplist(@JsonProperty("users") List<User> users, @JsonProperty("scores") List<ToplistEntry> scores) {
        this.users = users;
        this.scores = scores;
    }
}