package hu.bets.apigateway.command.bets;

import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.command.CommandBase;
import hu.bets.apigateway.command.util.RequestRunner;
import hu.bets.apigateway.model.bets.BetServiceErrorResponse;
import hu.bets.apigateway.model.bets.UserBet;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.util.json.Json;
import hu.bets.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class SendUserBetsCommand extends CommandBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendUserBetsCommand.class);
    private static final String USER_BETS_PATH = "/bets/football/v1/bet";
    private static final Json JSON = new Json();

    private final UserBet payload;

    public SendUserBetsCommand(ServiceResolverService resolverService, UserBet payload) {
        super(HystrixCommandGroupKey.Factory.asKey("BETS"), resolverService);
        this.payload = payload;
    }

    @Override
    protected String run() throws Exception {
        payload.setToken("empty-token");

        String endpoint = getFullEndpoint(Services.BETS, USER_BETS_PATH);
        Optional<String> result = new RequestRunner().runRequest(endpoint, JSON.toJson(payload));
        if (result.isPresent()) {
            LOGGER.info("Retrieved response from {}. Response was: {}", endpoint, result.get());
            return result.get();
        }

        return getFallback();
    }

    @Override
    protected String getFallback() {
        return JSON.toJson(new BetServiceErrorResponse("Unable to send user bets to the Bets-Service.", "token"));
    }
}
