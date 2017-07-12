package hu.bets.apigateway.service;

import com.netflix.hystrix.HystrixCommand;
import hu.bets.apigateway.command.CommandFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class SchedulesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesService.class);

    private final ServiceResolverService resolverService;
    private final CommandFacade commandFacade;
    private ClubBadgeResolverService badgeResolverService;

    public SchedulesService(ServiceResolverService resolverService, CommandFacade commandFacade, ClubBadgeResolverService badgeResolverService) {
        this.resolverService = resolverService;
        this.commandFacade = commandFacade;
        this.badgeResolverService = badgeResolverService;
    }

    public String getAggregatetResult() {
        return null;
    }

    private String getSchedules() {
        LOGGER.info("Running command for retrieving schedules.");
        HystrixCommand<String> schedulesCommand = commandFacade.getRetrieveSchedulesCommand();
        String json = schedulesCommand.execute();
        LOGGER.info("Returning schedules as JSON: {}", json);

        return json;
    }

    private String getUserBets(String userId, List<String> matchIds) {
        LOGGER.info("Running command for retrieving bets.");
        HystrixCommand<String> schedulesCommand = commandFacade.getRetrieveBetsCommand(userId, matchIds);
        String json = schedulesCommand.execute();
        LOGGER.info("Returning bets as JSON: {}", json);

        return json;
    }

    private Map<String, String> getClubBadges(List<String> clubNames) {
        return badgeResolverService.resolveBadges(clubNames);
    }


}
