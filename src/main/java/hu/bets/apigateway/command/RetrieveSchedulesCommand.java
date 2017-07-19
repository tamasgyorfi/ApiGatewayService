package hu.bets.apigateway.command;

import com.google.gson.Gson;
import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.model.BetServiceErrorResponse;
import hu.bets.apigateway.model.ScheduleServiceErrorResponse;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.services.Services;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RetrieveSchedulesCommand extends CommandBase {

    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveSchedulesCommand.class);
    private static final String SCHEDULES_PATH = "/matches/football/v1/schedules";
    private static final String TOKEN = "to-be-filled";

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
        return GSON.toJson(new ScheduleServiceErrorResponse("Unable to retrieve schedules.", TOKEN));
    }

    private String buildPayload() {
        return GSON.toJson(new SchedulesRequest(TOKEN));
    }

    private class SchedulesRequest {
        private String token;

        SchedulesRequest(String token) {
            this.token = token;
        }
    }
}
