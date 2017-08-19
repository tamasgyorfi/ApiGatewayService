package hu.bets.apigateway.web.api;

import com.google.gson.Gson;
import hu.bets.apigateway.model.bets.BetServiceErrorResponse;
import hu.bets.apigateway.model.bets.UserBet;
import hu.bets.apigateway.service.bets.BetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("aggregator/v1/bets")
@Component
public class BetsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BetsResource.class);
    private static final Gson GSON = new Gson();

    private final BetsService betsService;

    public BetsResource(BetsService betsService) {
        this.betsService = betsService;
    }

    @Path("userBets")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public String sendBets(String payload) {
        LOGGER.info("Incoming user bets received: {}", payload);
        try {
            UserBet response = GSON.fromJson(payload, UserBet.class);
            LOGGER.info("User bets successfully sent to Best-Service. Result was: {}", response);
            return betsService.sendBetsToBetService(response);
        } catch (Exception e) {
            LOGGER.error("Unable to send user bets to Bets-Service.", e);
            return GSON.toJson(new BetServiceErrorResponse("Unable to send user bets to the Bets-Service.", "token"));
        }
    }
}
