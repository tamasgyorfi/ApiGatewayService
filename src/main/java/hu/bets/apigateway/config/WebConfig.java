package hu.bets.apigateway.config;

import hu.bets.apigateway.service.bets.BetsService;
import hu.bets.apigateway.service.schedules.SchedulesService;
import hu.bets.apigateway.service.users.UsersService;
import hu.bets.apigateway.web.api.BetsResource;
import hu.bets.apigateway.web.api.SchedulesResource;
import hu.bets.apigateway.web.api.UsersResource;
import hu.bets.common.config.CommonWebConfig;
import hu.bets.common.config.model.Resources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CommonWebConfig.class)
public class WebConfig {

    @Bean
    public Resources resources(SchedulesResource schedulesBetResource,
                               BetsResource betsResource, UsersResource usersResource) {
        return new Resources()
                .addResource(schedulesBetResource)
                .addResource(betsResource)
                .addResource(usersResource);
    }

    @Bean
    public SchedulesResource footballBetResource(SchedulesService schedulesService) {
        return new SchedulesResource(schedulesService);
    }

    @Bean
    public BetsResource betsResource(BetsService betsService) {
        return new BetsResource(betsService);
    }

    @Bean
    public UsersResource usersResource(UsersService usersService) {
        return new UsersResource(usersService);
    }
}
