package hu.bets.apigateway.command;

import com.google.gson.Gson;
import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.model.BetServiceErrorResponse;
import hu.bets.apigateway.model.UserBet;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.services.Services;
import org.apache.http.client.methods.HttpPost;

import java.util.Optional;

public class SendUserBetsCommand extends CommandBase {

    private static final String USER_BETS_PATH = "/bets/football/v1/bet";
    private static final Gson GSON = new Gson();

    private final UserBet payload;

    SendUserBetsCommand(ServiceResolverService resolverService, UserBet payload) {
        super(HystrixCommandGroupKey.Factory.asKey("BETS"), resolverService);
        this.payload = payload;
    }

    @Override
    protected String run() throws Exception {
        payload.setToken("empty-token");

        String endpoint = getFullEndpoint(Services.BETS, USER_BETS_PATH);
        Optional<HttpPost> post = makePost(endpoint, GSON.toJson(payload));

        return post.map(this::runPost).orElseGet(this::getFallback);
    }

    @Override
    protected String getFallback() {
        return GSON.toJson(new BetServiceErrorResponse("Unable to send user bets to the Bets-Service.", "token"));
    }

}
