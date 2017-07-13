package hu.bets.apigateway.service;

import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.netflix.hystrix.HystrixCommand;
import hu.bets.apigateway.command.CommandFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.JsonPath.read;

public class SchedulesService {

    private static final String CREST_ADDRESS_PREFIX = "https://raw.githubusercontent.com/toptipr/multimedia/master/crests/";
    private static final String FILE_FORMAT = ".PNG";

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesService.class);
    private static final String MATCHES_PATH = "$.matches";
    private static final String MATCH_IDS_PATH = "$..matchId";
    private static final String CLUB_NAMES_PATH = "$..name";
    private static final String BET_PAYLOAD_PATH = "$.payload";

    private final CommandFacade commandFacade;
    private ClubBadgeResolverService badgeResolverService;

    public SchedulesService(CommandFacade commandFacade, ClubBadgeResolverService badgeResolverService) {
        this.commandFacade = commandFacade;
        this.badgeResolverService = badgeResolverService;
    }

    public String getAggregatetResult(String userId) {

        String schedulesJson = getSchedules();
        Object schedulesDoc = Configuration.defaultConfiguration().jsonProvider().parse(schedulesJson);
        List<String> matches = read(schedulesDoc, MATCHES_PATH);
        List<String> matchIds = read(schedulesDoc, MATCH_IDS_PATH);
        List<String> teamNames = read(schedulesDoc, CLUB_NAMES_PATH);

        String userBetsJson = getUserBets(userId, matchIds);
        Object userBetsDoc = Configuration.defaultConfiguration().jsonProvider().parse(userBetsJson);
        List<String> userBets = read(userBetsDoc, BET_PAYLOAD_PATH);

        return new Gson().toJson(new Schedules(matches, userBets, new Crests(getClubBadges(teamNames), CREST_ADDRESS_PREFIX, FILE_FORMAT)));
    }

    protected String getSchedules() {
        LOGGER.info("Running command for retrieving schedules.");
        HystrixCommand<String> schedulesCommand = commandFacade.getRetrieveSchedulesCommand();
        String json = schedulesCommand.execute();
        LOGGER.info("Returning schedules as JSON: {}", json);

        return json;
    }

    protected String getUserBets(String userId, List<String> matchIds) {
        LOGGER.info("Running command for retrieving bets.");
        HystrixCommand<String> schedulesCommand = commandFacade.getRetrieveBetsCommand(userId, matchIds);
        String json = schedulesCommand.execute();
        LOGGER.info("Returning bets as JSON: {}", json);

        return json;
    }

    private Map<String, String> getClubBadges(List<String> clubNames) {
        return badgeResolverService.resolveBadges(clubNames);
    }

    private static final class Schedules {
        private final List<String> schedules;
        private final List<String> bets;
        private final Crests crests;

        Schedules(List<String> schedules, List<String> bets, Crests crests) {
            this.schedules = schedules;
            this.bets = bets;
            this.crests = crests;
        }
    }

    private static class Crests {
        private final Map<String, String> crests;
        private final String baseUrl;
        private final String format;

        Crests(Map<String, String> crests, String baseUrl, String format) {

            this.crests = crests;
            this.baseUrl = baseUrl;
            this.format = format;
        }
    }

}
