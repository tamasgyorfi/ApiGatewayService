package hu.bets.apigateway.command.scores;

import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.command.CommandBase;
import hu.bets.apigateway.command.bets.SendUserBetsCommand;
import hu.bets.apigateway.command.util.RequestRunner;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.util.json.Json;
import hu.bets.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserScoresCommand extends CommandBase<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendUserBetsCommand.class);
    private static final String USER_SCORES_PATH = "/scores/football/v1/toplist";
    private static final Json JSON = new Json();
    private final List<String> userIds;

    public UserScoresCommand(ServiceResolverService resolverService, List<String> userIds) {
        super(HystrixCommandGroupKey.Factory.asKey("TOPLIST"), resolverService);
        this.userIds = userIds;
    }

    @Override
    protected String run() {
        UserScoreRequest req = new UserScoreRequest(userIds, "");

        String endpoint = getFullEndpoint(Services.SCORES, USER_SCORES_PATH);
        String result = new RequestRunner().runRequest(endpoint, JSON.toJson(req));

        LOGGER.info("Retrieved response from {}. Response was: {}", endpoint, result);
        return result;
    }

    @Override
    protected String getFallback() {
        return "{\"entries\":[], \"token\":\"empty\"}";
    }

    private class UserScoreRequest {
        private final List<String> userIds;
        private final String token;

        public UserScoreRequest(List<String> userIds, String token) {
            this.userIds = userIds;
            this.token = token;
        }
    }
}
