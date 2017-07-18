package hu.bets.apigateway.service;

import hu.bets.apigateway.model.UserBet;

public interface BetsService {

    /**
     * Routes the incoming payload from the client to the bets service.
     *
     * @param payload bets made by a user.
     */
    String sendBetsToBetService(UserBet payload);
}
