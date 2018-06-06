package hu.bets.apigateway.web.api;

import hu.bets.apigateway.model.bets.BetServiceErrorResponse;
import hu.bets.apigateway.model.bets.UserBet;
import hu.bets.apigateway.service.bets.BetsService;
import hu.bets.common.util.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("aggregator/v1/bets")
@Component
public class BetsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BetsResource.class);
    private static final Json JSON = new Json();

    private final BetsService betsService;

    public BetsResource(BetsService betsService) {
        this.betsService = betsService;
    }

    @Path("user-bets")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response sendBets(String payload) {
        LOGGER.info("Incoming user bets received: {}", payload);
        try {
            UserBet response = JSON.fromJson(payload, UserBet.class);
            LOGGER.info("User bets successfully sent to Best-Service. Result was: {}", response);
            return Response.ok().entity(betsService.sendBetsToBetService(response)).build();
        } catch (Exception e) {
            LOGGER.error("Unable to send user bets to Bets-Service.", e);
            return Response.serverError().entity(JSON.toJson(new BetServiceErrorResponse("Unable to send user bets to the Bets-Service.", "token"))).build();
        }
    }
}
