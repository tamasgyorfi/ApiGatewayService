package hu.bets.apigateway.service;

import hu.bets.apigateway.model.Schedules;

/**
 * Created by tamas.gyorfi on 18/07/2017.
 */
public interface SchedulesService {
    Schedules getAggregatedResult(String userId);
}
