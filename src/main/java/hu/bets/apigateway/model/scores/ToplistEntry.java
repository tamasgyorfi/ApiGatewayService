package hu.bets.apigateway.model.scores;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ToplistEntry {

    @JsonCreator
    public ToplistEntry(@JsonProperty("userId") String userId, @JsonProperty("points") long points) {
        this.userId = userId;
        this.points = points;
    }

    private String userId;
    private long points;

}
