package hu.bets.apigateway.web.api;

import hu.bets.apigateway.model.scores.Toplist;
import hu.bets.apigateway.service.scores.ScoresService;
import hu.bets.apigateway.web.model.users.Response;
import hu.bets.common.util.json.Json;
import hu.bets.common.util.json.JsonParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("aggregator/v1/scores")
public class ScoresResource {

    private static final Json JSON = new Json();
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoresResource.class);
    private ScoresService scoresService;

    public ScoresResource(ScoresService scoresService) {
        this.scoresService = scoresService;
    }


    @Path("toplist/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public javax.ws.rs.core.Response getToplist(@PathParam("userId") String userId, String payload) {
        LOGGER.info("Toplist endpoint called with payload: {}", payload);
        try {
            Toplist response = scoresService.getToplist(userId);
            LOGGER.info("Toplist successfully retrieved. Resulting list is: {}", response);

            return javax.ws.rs.core.Response.ok()
                    .entity(JSON.toJson(Response.success(response, "empty_token")))
                    .header("Access-Control-Allow-Origin", "http://www.toptipr.com")
                    .header("Access-Control-Allow-Origin", "https://football-frontend.herokuapp.com")
                    .build();
        } catch (JsonParsingException e) {
            return javax.ws.rs.core.Response.status(400)
                    .entity("Invalid JSON request received")
                    .header("Access-Control-Allow-Origin", "http://www.toptipr.com")
                    .header("Access-Control-Allow-Origin", "https://football-frontend.herokuapp.com")
                    .build();
        } catch (Exception e) {
            LOGGER.error("Exception caught while retrieving user toplist. ", e);
            return javax.ws.rs.core.Response.serverError()
                    .entity(JSON.toJson(Response.failure("Unable to retrieve user toplist. " + e.getMessage(), "empty_token")))
                    .header("Access-Control-Allow-Origin", "http://www.toptipr.com")
                    .header("Access-Control-Allow-Origin", "https://football-frontend.herokuapp.com")
                    .build();
        }
    }

}
