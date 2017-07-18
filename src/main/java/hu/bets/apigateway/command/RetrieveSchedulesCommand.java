package hu.bets.apigateway.command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.services.Services;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RetrieveSchedulesCommand extends CommandBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveSchedulesCommand.class);
    private static final String SCHEDULES_PATH = "/matches/football/v1/schedules";

    RetrieveSchedulesCommand(ServiceResolverService serviceResolverService) {
        super(HystrixCommandGroupKey.Factory.asKey("SCHEDULES"), serviceResolverService);
    }

    @Override
    protected String run() throws Exception {
        String fullEndpoint = getFullEndpoint(Services.MATCHES, SCHEDULES_PATH);
        Optional<HttpPost> httpPost = makePost(fullEndpoint, buildPayload());
        if (httpPost.isPresent()) {
            LOGGER.info("Running http post to retrieve schedules.");
            return runPost(httpPost.get());
        }

        return getFallback();
    }

    @Override
    protected String getFallback() {
        return String.format("{\"matches\": [], \"token\":\"%s\",\"error\": \"Unable to retrieve schedules.\"}", "security-token-to-be-filled");
    }

    private String buildPayload() {
        return "\"token\":\"\"";
    }
}
