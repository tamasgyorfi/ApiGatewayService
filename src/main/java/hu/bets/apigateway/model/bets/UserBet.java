package hu.bets.apigateway.model.bets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBet {

    private String userId;
    private List<Bet> bets;
    private String token;

    @JsonCreator
    public UserBet(@JsonProperty("userId") String userId, @JsonProperty("bets") List<Bet> bets) {
        this.userId = userId;
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

        if (userId != null ? !userId.equals(userBet.userId) : userBet.userId != null) return false;
        return bets != null ? bets.equals(userBet.bets) : userBet.bets == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (bets != null ? bets.hashCode() : 0);
        return result;
    }
}
