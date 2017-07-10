package hu.bets.apigateway.config;

import hu.bets.apigateway.command.CommandFacade;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.util.servicediscovery.DefaultEurekaFacade;
import hu.bets.common.util.servicediscovery.EurekaFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public EurekaFacade eurekaFacade() {
        return new DefaultEurekaFacade();
    }

    @Bean
    public CommandFacade commandFacade(ServiceResolverService resolverService) {
        return new CommandFacade(resolverService);
    }

    @Bean
    public ServiceResolverService serviceResolver(EurekaFacade eurekaFacade) {
        return new ServiceResolverService(eurekaFacade);
    }
}
