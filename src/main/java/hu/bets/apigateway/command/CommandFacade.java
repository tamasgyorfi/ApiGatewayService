package hu.bets.apigateway.command;

import com.netflix.hystrix.HystrixCommand;
import hu.bets.apigateway.service.ServiceResolverService;

import java.util.Map;

public class CommandFacade {

    private ServiceResolverService resolverService;

    public CommandFacade(ServiceResolverService resolverService) {
        this.resolverService = resolverService;
    }

    public HystrixCommand<String> getRetrieveSchedulesCommand() {
        return new RetrieveSchedulesCommand(resolverService);
    }
}
