package hu.bets.apigateway.command;

import com.netflix.hystrix.HystrixCommand;
import hu.bets.apigateway.service.ServiceResolverService;

import java.util.List;

public class CommandFacade {

    private ServiceResolverService resolverService;

    public CommandFacade(ServiceResolverService resolverService) {
        this.resolverService = resolverService;
    }

    public HystrixCommand<String> getRetrieveSchedulesCommand() {
        return new RetrieveSchedulesCommand(resolverService);
    }

    public HystrixCommand<String> getRetrieveBetsCommand(String userId, List<String> matchIds) {
        return new RetrieveUserBetsCommand(resolverService, userId, matchIds);
    }

    public HystrixCommand<String> sendUserBets(String payload) {
        return new SendUserBetsCommand(resolverService, payload);
    }
}
