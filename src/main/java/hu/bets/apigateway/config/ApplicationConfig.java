package hu.bets.apigateway.config;

import hu.bets.apigateway.command.CommandFacade;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.apigateway.service.bets.BetsService;
import hu.bets.apigateway.service.bets.DefaultBetsService;
import hu.bets.apigateway.service.schedules.ClubBadgeResolverService;
import hu.bets.apigateway.service.schedules.DefaultSchedulesService;
import hu.bets.apigateway.service.schedules.SchedulesService;
import hu.bets.apigateway.service.users.DefaultUsersService;
import hu.bets.apigateway.service.users.UsersService;
import hu.bets.common.util.EnvironmentVarResolver;
import hu.bets.common.util.schema.SchemaValidator;
import hu.bets.servicediscovery.EurekaFacade;
import hu.bets.servicediscovery.EurekaFacadeImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public EurekaFacade eurekaFacade() {
        return new EurekaFacadeImpl(EnvironmentVarResolver.getEnvVar("EUREKA_URL", () -> {
            throw new IllegalStateException();
        }));
    }

    @Bean
    public CommandFacade commandFacade(ServiceResolverService resolverService) {
        return new CommandFacade(resolverService);
    }

    @Bean
    public ServiceResolverService serviceResolver(EurekaFacade eurekaFacade) {
        return new ServiceResolverService(eurekaFacade);
    }

    @Bean
    public ClubBadgeResolverService clubBadgeResolverService() {
        return new ClubBadgeResolverService();
    }

    @Bean
    public SchedulesService schedulesService(CommandFacade commandFacade, ClubBadgeResolverService badgeResolverService) {
        return new DefaultSchedulesService(commandFacade, badgeResolverService);
    }

    @Bean
    public BetsService betsService(CommandFacade commandFacade) {
        return new DefaultBetsService(commandFacade);
    }

    @Bean
    public UsersService usersService(CommandFacade commandFacade) {
        return new DefaultUsersService(commandFacade);
    }

    @Bean
    public SchemaValidator schemaValidator() {
        return new SchemaValidator();
    }
}
