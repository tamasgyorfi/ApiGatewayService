package hu.bets.apigateway.web.api;

import com.google.gson.Gson;
import hu.bets.apigateway.model.Schedules;
import hu.bets.apigateway.service.SchedulesService;
import hu.bets.apigateway.web.model.SchedulesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("aggregator/v1/schedules")
@Component
public class SchedulesResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesResource.class);

    @Autowired
    private SchedulesService schedulesService;

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
        LOGGER.info("Incoming request for schedules {}.", payload);
        SchedulesRequest schedulesRequest = new Gson().fromJson(payload, SchedulesRequest.class);
        LOGGER.info("Extracted userId is: {}", schedulesRequest.getUserId());

        Schedules retVal = schedulesService.getAggregatedResult(schedulesRequest.getUserId());
        String resultingJson = new Gson().toJson(retVal);
        LOGGER.info("Returning payload for request made by user {}. Payload is: {}", schedulesRequest.getUserId(), resultingJson);

        return resultingJson;
    }


}
