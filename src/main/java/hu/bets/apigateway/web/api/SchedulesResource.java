package hu.bets.apigateway.web.api;

import hu.bets.apigateway.model.schedules.ScheduleServiceErrorResponse;
import hu.bets.apigateway.model.schedules.Schedules;
import hu.bets.apigateway.service.schedules.SchedulesService;
import hu.bets.apigateway.web.model.schedules.SchedulesRequest;
import hu.bets.common.util.json.Json;
import hu.bets.common.util.json.JsonParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("aggregator/v1/schedules")
@Component
public class SchedulesResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesResource.class);
    private static final Json JSON = new Json();

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

    @Path("upcoming-matches")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response getSchedules(String payload) {
        try {
            LOGGER.info("Incoming request for schedules {}.", payload);
            SchedulesRequest schedulesRequest = JSON.fromJson(payload, SchedulesRequest.class);
            LOGGER.info("Extracted userId is: {}", schedulesRequest.getUserId());

            Schedules retVal = defaultSchedulesService.getAggregatedResult(schedulesRequest.getUserId());
            String resultingJson = JSON.toJson(retVal);
            LOGGER.info("Returning payload for request made by user {}. Payload is: {}", schedulesRequest.getUserId(), resultingJson);

            return Response.ok()
                    .entity(resultingJson)
                    .header("Access-Control-Allow-Origin", "http://toptipr.com")
                    .header("Access-Control-Allow-Origin", "https://football-frontend.herokuapp.com")
                    .build();
        } catch (JsonParsingException e) {
            return Response.status(400)
                    .entity("Invalid JSON request received")
                    .header("Access-Control-Allow-Origin", "http://toptipr.com")
                    .header("Access-Control-Allow-Origin", "https://football-frontend.herokuapp.com")
                    .build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(JSON.toJson(new ScheduleServiceErrorResponse("Unable to retrieve schedules. " + e.getMessage(), "")))
                    .header("Access-Control-Allow-Origin", "http://toptipr.com")
                    .header("Access-Control-Allow-Origin", "https://football-frontend.herokuapp.com")
                    .build();
        }
    }


}
