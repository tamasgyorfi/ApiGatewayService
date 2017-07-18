package hu.bets.apigateway.model;

import java.util.List;

public class UserBet {

    private String userId;
    private List<Bet> bets;
    private String token;

    public UserBet(String userId, List<Bet> bets) {
        this.userId = userId;
        this.bets = bets;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
