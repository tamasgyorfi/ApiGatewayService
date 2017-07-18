package hu.bets.apigateway.command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.services.Services;
import org.apache.http.client.methods.HttpPost;

import java.util.Optional;

public class SendUserBetsCommand extends CommandBase {

    private static final String USER_BETS_PATH = "/bets/football/v1/bet";

    private final String payload;

    SendUserBetsCommand(ServiceResolverService resolverService, String payload) {
        super(HystrixCommandGroupKey.Factory.asKey("BETS"), resolverService);
        this.payload = payload;
    }

    @Override
    protected String run() throws Exception {
        String endpoint = getFullEndpoint(Services.BETS, USER_BETS_PATH);
        Optional<HttpPost> post = makePost(endpoint, payload);

        return post.map(this::runPost).orElseGet(this::getFallback);
    }

    @Override
    protected String getFallback() {
        return "Unable to send user bets to the Bets-Service.";
    }

}
