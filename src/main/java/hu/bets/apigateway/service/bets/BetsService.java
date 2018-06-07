package hu.bets.apigateway.service.bets;

import hu.bets.apigateway.model.bets.UserBet;

public interface BetsService {

    /**
     * Routes the incoming payload from the client to the bets service.
     *
     * @param userId
     * @param payload bets made by a user.
     */
    String sendBetsToBetService(String userId, UserBet payload);
}
