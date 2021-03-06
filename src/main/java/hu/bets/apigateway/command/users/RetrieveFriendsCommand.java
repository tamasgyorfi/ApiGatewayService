package hu.bets.apigateway.command.users;

import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.command.CommandBase;
import hu.bets.apigateway.command.CommandException;
import hu.bets.apigateway.command.bets.SendUserBetsCommand;
import hu.bets.apigateway.command.util.RequestRunner;
import hu.bets.apigateway.model.users.User;
import hu.bets.apigateway.model.users.UserServiceErrorResponse;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.util.json.Json;
import hu.bets.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetrieveFriendsCommand extends CommandBase<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendUserBetsCommand.class);
    private static final String USER_FRIENDS_PATH = "/users/football/v1/friends/%s";
    private static final Json JSON = new Json();
    private final String userId;

    public RetrieveFriendsCommand(ServiceResolverService resolverService, String userId) {
        super(HystrixCommandGroupKey.Factory.asKey("FRIENDS"), resolverService);
        this.userId = userId;
    }

    @Override
    protected String run() {
        User user = new User(userId, "", "");
        user.setToken("empty_token");

        String endpoint = getFullEndpoint(Services.USERS, String.format(USER_FRIENDS_PATH, userId));
        String result = new RequestRunner().runRequest(endpoint, JSON.toJson(user));

        LOGGER.info("Retrieved response from {}. Response was: {}", endpoint, result);
        return result;
    }

    @Override
    protected String getFallback() {
        throw new CommandException("Falling back...");
    }
}
