package hu.bets.apigateway.command.users;

import com.google.gson.Gson;
import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.command.CommandBase;
import hu.bets.apigateway.command.bets.SendUserBetsCommand;
import hu.bets.apigateway.command.util.RequestRunner;
import hu.bets.apigateway.model.users.UserServiceErrorResponse;
import hu.bets.apigateway.model.users.FriendsUpdate;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UpdateFriendsCommand extends CommandBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendUserBetsCommand.class);
    private static final String MODIFY_FRIENDS_PATH = "/users/football/v1/update";
    private static final Gson GSON = new Gson();
    private final FriendsUpdate friendsUpdate;


    public UpdateFriendsCommand(ServiceResolverService resolverService, FriendsUpdate friendsUpdate) {
        super(HystrixCommandGroupKey.Factory.asKey("MODIFY_FRIENDS"), resolverService);
        this.friendsUpdate = friendsUpdate;
    }

    @Override
    protected String run() throws Exception {
        friendsUpdate.setToken("empty_token");
        String endpoint = getFullEndpoint(Services.USERS, MODIFY_FRIENDS_PATH);
        Optional<String> result = new RequestRunner().runRequest(endpoint, GSON.toJson(friendsUpdate));
        if (result.isPresent()) {
            LOGGER.info("Retrieved response from {}. Response was: {}", endpoint, result.get());
            return result.get();
        }

        return getFallback();
    }

    @Override
    protected String getFallback() {
        return GSON.toJson(new UserServiceErrorResponse("Unable to modify user's friends.", "token"));
    }

}
