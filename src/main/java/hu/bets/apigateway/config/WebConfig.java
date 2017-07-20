package hu.bets.apigateway.config;

import hu.bets.apigateway.service.BetsService;
import hu.bets.apigateway.service.SchedulesService;
import hu.bets.apigateway.web.api.BetsResource;
import hu.bets.apigateway.web.api.SchedulesResource;
import hu.bets.common.config.CommonWebConfig;
import hu.bets.common.config.model.Resources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CommonWebConfig.class)
public class WebConfig {

    @Bean
    public Resources resources(SchedulesResource schedulesBetResource, BetsResource betsResource) {
        return new Resources()
                .addResource(schedulesBetResource)
                .addResource(betsResource);
    }

    @Bean
    public SchedulesResource footballBetResource(SchedulesService schedulesService) {
        return new SchedulesResource(schedulesService);
    }

    @Bean
    public BetsResource betsResource(BetsService betsService) {
        return new BetsResource(betsService);
    }
}
