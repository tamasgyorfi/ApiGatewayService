package hu.bets.apigateway.web.api;

import com.netflix.hystrix.HystrixCommand;
import hu.bets.apigateway.command.CommandFacade;
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
    private CommandFacade commandFacade;

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
        HystrixCommand<String> schedulesCommand = commandFacade.getRetrieveSchedulesCommand();
        LOGGER.info("Incoming request for schedules {}.", payload);
        return schedulesCommand.execute();
    }
}
