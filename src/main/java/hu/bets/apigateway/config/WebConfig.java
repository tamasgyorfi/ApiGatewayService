package hu.bets.apigateway.config;

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
    public Resources resources(SchedulesResource schedulesBetResource) {
        return new Resources().addResource(schedulesBetResource);
    }

    @Bean
    public SchedulesResource footballBetResource() {
        return new SchedulesResource();
    }

}
