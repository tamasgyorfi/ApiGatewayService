package hu.bets.apigateway.command.bets;

import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.command.CommandBase;
import hu.bets.apigateway.command.schedules.RetrieveSchedulesCommand;
import hu.bets.apigateway.command.util.RequestRunner;
import hu.bets.apigateway.model.bets.BetServiceErrorResponse;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.util.json.Json;
import hu.bets.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class RetrieveUserBetsCommand extends CommandBase {

    private static final Json JSON = new Json();
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveSchedulesCommand.class);
    private static final String USER_BETS_PATH = "/bets/football/v1/userBets";

    private String userId;
    private List<String> matchIds;

    public RetrieveUserBetsCommand(ServiceResolverService serviceResolverService, String userId, List<String> matchIds) {
        super(HystrixCommandGroupKey.Factory.asKey("BETS"), serviceResolverService);
        this.userId = userId;
        this.matchIds = matchIds;
    }

    @Override
    protected String run() throws Exception {
        String fullEndpoint = getFullEndpoint(Services.BETS, USER_BETS_PATH);
        Optional<String> result = new RequestRunner().runRequest(fullEndpoint, buildPayload());
        if (result.isPresent()) {
            LOGGER.info("Retrieved response from {}. Response was: {}", fullEndpoint, result.get());
            return result.get();
        }

        return getFallback();
    }

    private String buildPayload() {
        return JSON.toJson(new UserBetsRequest(userId, matchIds, "token-to-be-filled"));
    }

    @Override
    protected String getFallback() {
        return JSON.toJson(new BetServiceErrorResponse("Unable to retrieve user bets.", "security-token-to-be-filled"));
    }

    private static class UserBetsRequest {
        private String userId;
        private List<String> ids;
        private String token;

        UserBetsRequest(String userId, List<String> ids, String token) {
            this.userId = userId;
            this.ids = ids;
            this.token = token;
        }
    }
}
