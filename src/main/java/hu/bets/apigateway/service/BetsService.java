package hu.bets.apigateway.service;

public interface BetsService {

    /**
     * Routes the incoming payload from the client to the bets service.
     *
     * @param payload bets in JSON format.
     */
    String sendBetsToBetService(String payload);
}
