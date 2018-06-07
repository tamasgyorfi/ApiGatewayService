package hu.bets.apigateway.model.bets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBet {

    private List<Bet> bets;
    private String token;

    @JsonCreator
    public UserBet(@JsonProperty("userId") String userId, @JsonProperty("bets") List<Bet> bets) {
        this.bets = bets;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBet userBet = (UserBet) o;
        return Objects.equals(bets, userBet.bets) &&
                Objects.equals(token, userBet.token);
    }

    @Override
    public int hashCode() {

        return Objects.hash(bets, token);
    }
}
