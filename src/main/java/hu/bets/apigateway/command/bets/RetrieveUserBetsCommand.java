package hu.bets.apigateway.command.bets;

import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.command.CommandBase;
import hu.bets.apigateway.command.CommandException;
import hu.bets.apigateway.command.schedules.RetrieveSchedulesCommand;
import hu.bets.apigateway.command.util.RequestRunner;
import hu.bets.apigateway.model.bets.BetServiceErrorResponse;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.util.json.Json;
import hu.bets.model.filter.Field;
import hu.bets.model.filter.Filter;
import hu.bets.model.filter.MultiEqualsFilter;
import hu.bets.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class RetrieveUserBetsCommand extends CommandBase {

    private static final Json JSON = new Json();
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveSchedulesCommand.class);
    private static final String USER_BETS_PATH = "/bets/football/v1/%s/user-bets";

    private String userId;
    private List<String> matchIds;

    public RetrieveUserBetsCommand(ServiceResolverService serviceResolverService, String userId, List<String> matchIds) {
        super(HystrixCommandGroupKey.Factory.asKey("BETS"), serviceResolverService);
        this.userId = userId;
        this.matchIds = matchIds;
    }

    @Override
    protected String run() throws Exception {
        String fullEndpoint = getFullEndpoint(Services.BETS, String.format(USER_BETS_PATH, userId));
        LOGGER.info("Endpoint is: {}", fullEndpoint);
        String result = new RequestRunner().runRequest(fullEndpoint, buildPayload());

        LOGGER.info("Retrieved response from {}. Response was: {}", fullEndpoint, result);
        return result;
    }

    private String buildPayload() {
        return JSON.toJson(new UserBetsRequest(matchIds, "token-to-be-filled"));
    }

    @Override
    protected String getFallback() {
        throw new CommandException("Falling back...");
    }

    private static class UserBetsRequest {
        private List<Filter> filters;
        private String token;

        UserBetsRequest(List<String> ids, String token) {
            filters = Arrays.asList(new MultiEqualsFilter(Field.MATCH_ID, ids));
            this.token = token;
        }
    }
}
