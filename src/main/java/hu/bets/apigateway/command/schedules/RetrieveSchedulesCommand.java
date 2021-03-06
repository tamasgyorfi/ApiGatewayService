package hu.bets.apigateway.command.schedules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.command.CommandBase;
import hu.bets.apigateway.command.CommandException;
import hu.bets.apigateway.command.util.RequestRunner;
import hu.bets.apigateway.model.schedules.ScheduleServiceErrorResponse;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.util.json.Json;
import hu.bets.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RetrieveSchedulesCommand extends CommandBase {

    private static final Json JSON = new Json();
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveSchedulesCommand.class);
    private static final String SCHEDULES_PATH = "/matches/football/v1/schedules";
    private static final String TOKEN = "to-be-filled";

    public RetrieveSchedulesCommand(ServiceResolverService serviceResolverService) {
        super(HystrixCommandGroupKey.Factory.asKey("SCHEDULES"), serviceResolverService);
    }

    @Override
    protected String run() throws Exception {
        String fullEndpoint = getFullEndpoint(Services.MATCHES, SCHEDULES_PATH);
        String result = new RequestRunner().runRequest(fullEndpoint, buildPayload());

        LOGGER.info("Retrieved response from {}. Response was: {}", fullEndpoint, result);
        return result;
    }

    @Override
    protected String getFallback() {
        throw new CommandException("Falling back...");
    }

    private String buildPayload() {
        return JSON.toJson(new SchedulesRequest(TOKEN));
    }

    private class SchedulesRequest {
        private String token;

        @JsonCreator
        SchedulesRequest(@JsonProperty("token") String token) {
            this.token = token;
        }
    }
}
