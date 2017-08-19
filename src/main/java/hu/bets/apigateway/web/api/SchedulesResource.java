package hu.bets.apigateway.web.api;

import com.google.gson.Gson;
import hu.bets.apigateway.model.schedules.ScheduleServiceErrorResponse;
import hu.bets.apigateway.model.schedules.Schedules;
import hu.bets.apigateway.service.schedules.SchedulesService;
import hu.bets.apigateway.web.model.schedules.SchedulesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("aggregator/v1/schedules")
@Component
public class SchedulesResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesResource.class);
    private static final Gson GSON = new Gson();

    private SchedulesService defaultSchedulesService;

    public SchedulesResource(SchedulesService defaultSchedulesService) {
        this.defaultSchedulesService = defaultSchedulesService;
    }

    @Path("info")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getInfo() {
        return "<html><body><h1>Api gateway up and running!</h1></body></html>";
    }

    @Path("upcomings")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public String getSchedules(String payload) {
        try {
            LOGGER.info("Incoming request for schedules {}.", payload);
            SchedulesRequest schedulesRequest = GSON.fromJson(payload, SchedulesRequest.class);
            LOGGER.info("Extracted userId is: {}", schedulesRequest.getUserId());

            Schedules retVal = defaultSchedulesService.getAggregatedResult(schedulesRequest.getUserId());
            String resultingJson = GSON.toJson(retVal);
            LOGGER.info("Returning payload for request made by user {}. Payload is: {}", schedulesRequest.getUserId(), resultingJson);

            return resultingJson;
        } catch (Exception e) {
            return GSON.toJson(new ScheduleServiceErrorResponse("Unable to retrieve schedules. " + e.getMessage(), ""));
        }
    }


}
