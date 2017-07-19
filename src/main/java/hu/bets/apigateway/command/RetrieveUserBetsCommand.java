package hu.bets.apigateway.command;

import com.google.gson.Gson;
import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.model.BetServiceErrorResponse;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.services.Services;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class RetrieveUserBetsCommand extends CommandBase {

    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveSchedulesCommand.class);
    private static final String USER_BETS_PATH = "/bets/football/v1/userBets";

    private String userId;
    private List<String> matchIds;

    RetrieveUserBetsCommand(ServiceResolverService serviceResolverService, String userId, List<String> matchIds) {
        super(HystrixCommandGroupKey.Factory.asKey("BETS"), serviceResolverService);
        this.userId = userId;
        this.matchIds = matchIds;
    }

    @Override
    protected String run() throws Exception {
        String fullEndpoint = getFullEndpoint(Services.BETS, USER_BETS_PATH);
        Optional<HttpPost> httpPost = makePost(fullEndpoint, buildPayload());
        if (httpPost.isPresent()) {
            LOGGER.info("Running http post to retrieve bets.");
            return runPost(httpPost.get());
        }

        return getFallback();
    }

    private String buildPayload() {
        return GSON.toJson(new UserBetsRequest(userId, matchIds, "token-to-be-filled"));
    }

    @Override
    protected String getFallback() {
        return GSON.toJson(new BetServiceErrorResponse("Unable to retrieve user bets.", "security-token-to-be-filled"));
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
