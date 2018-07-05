package hu.bets.apigateway.web.api;

import hu.bets.apigateway.model.bets.BetServiceErrorResponse;
import hu.bets.apigateway.model.bets.UserBet;
import hu.bets.apigateway.service.bets.BetsService;
import hu.bets.common.util.json.Json;
import hu.bets.common.util.schema.InvalidScemaException;
import hu.bets.common.util.schema.SchemaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("aggregator/v1/bets")
@Component
public class BetsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BetsResource.class);
    private static final Json JSON = new Json();

    @Autowired
    protected SchemaValidator schemaValidator;

    private final BetsService betsService;

    public BetsResource(BetsService betsService) {
        this.betsService = betsService;
    }

    @Path("{userId}/user-bets")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response sendBets(@PathParam("userId") String userId, String payload) {
        LOGGER.info("Incoming user bets received: {}, {}", userId, payload);
        try {
            UserBet response = validateAndParse(payload);
            LOGGER.info("User bets successfully sent to Best-Service. Result was: {}", response);
            return Response.ok()
                    .entity(betsService.sendBetsToBetService(userId, response))
                    .header("Access-Control-Allow-Origin", "http://toptipr.com")
                    .header("Access-Control-Allow-Origin", "https://football-frontend.herokuapp.com")
                    .build();
        } catch (InvalidScemaException ise) {
            LOGGER.info("Invalid request detected. Validation errors were: {}", ise.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(JSON.toJson(new BetServiceErrorResponse(ise.getMessage(), "token")))
                    .header("Access-Control-Allow-Origin", "http://toptipr.com")
                    .header("Access-Control-Allow-Origin", "https://football-frontend.herokuapp.com")
                    .build();
        } catch (Exception e) {
            LOGGER.error("Unable to send user bets to Bets-Service.", e);
            return Response.serverError()
                    .entity(JSON.toJson(new BetServiceErrorResponse("Unable to send user bets to the Bets-Service.", "token")))
                    .header("Access-Control-Allow-Origin", "http://toptipr.com")
                    .header("Access-Control-Allow-Origin", "https://football-frontend.herokuapp.com")
                    .build();
        }
    }

    private UserBet validateAndParse(String input) {
        LOGGER.info("Validating incoming payload.");
        schemaValidator.validatePayload(input, "user-bets.request.schema.json");
        return JSON.fromJson(input, UserBet.class);
    }

}
