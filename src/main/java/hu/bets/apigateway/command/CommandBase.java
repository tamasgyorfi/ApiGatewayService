package hu.bets.apigateway.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.command.bets.SendUserBetsCommand;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CommandBase extends HystrixCommand<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendUserBetsCommand.class);
    private final ServiceResolverService resolverService;


    protected CommandBase(HystrixCommandGroupKey group, ServiceResolverService resolverService) {
        super(group);
        this.resolverService = resolverService;
    }

    protected String getFullEndpoint(Services service, String path) {
        String endpoint = resolverService.resolve(service);
        LOGGER.info("PATH is: {}", endpoint + path);
        return endpoint + path;
    }
}
