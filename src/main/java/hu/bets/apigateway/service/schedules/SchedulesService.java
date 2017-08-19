package hu.bets.apigateway.service.schedules;

import hu.bets.apigateway.model.schedules.Schedules;

public interface SchedulesService {
    Schedules getAggregatedResult(String userId);
}
