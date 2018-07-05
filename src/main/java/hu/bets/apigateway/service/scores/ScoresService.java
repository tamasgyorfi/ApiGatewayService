package hu.bets.apigateway.service.scores;

import hu.bets.apigateway.model.scores.Toplist;

public interface ScoresService {

    /**
     * Retrieves the league standing for the current user
     * @param userId
     * @return league standings in JSON
     */
    Toplist getToplist(String userId);

}
