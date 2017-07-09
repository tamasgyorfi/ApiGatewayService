package hu.bets.apigateway.web.api;

import com.netflix.hystrix.HystrixCommand;
import hu.bets.apigateway.command.CommandFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("aggregator/v1/schedules")
@Component
public class SchedulesEndpoint {

    @Autowired
    private CommandFacade commandFacade;

    @Path("info")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getInfo() {
        return "<html><body><h1>Api gateway up and running!</h1></body></html>";
    }

    @Path("upcoming")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getSchedules(String payload) {
        HystrixCommand<String> schedulesCommand = commandFacade.getRetrieveSchedulesCommand();
        return schedulesCommand.execute();
    }
}
