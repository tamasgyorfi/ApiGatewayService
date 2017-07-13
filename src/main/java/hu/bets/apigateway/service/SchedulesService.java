package hu.bets.apigateway.service;

import com.google.common.collect.Lists;
import com.jayway.jsonpath.Configuration;
import com.netflix.hystrix.HystrixCommand;
import hu.bets.apigateway.command.CommandFacade;
import hu.bets.apigateway.model.Crests;
import hu.bets.apigateway.model.Schedules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
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
    private static final String ERROR_PATH = "$.error";

    private final CommandFacade commandFacade;
    private ClubBadgeResolverService badgeResolverService;

    public SchedulesService(CommandFacade commandFacade, ClubBadgeResolverService badgeResolverService) {
        this.commandFacade = commandFacade;
        this.badgeResolverService = badgeResolverService;
    }

    public Schedules getAggregatetResult(String userId) {

        List<String> matches = null;
        List<String> matchIds = null;
        List<String> teamNames = null;
        List<String> userBets = null;
        String schedulesError = null;
        String betsError = null;

        try {
            String schedulesJson = getSchedules();
            LOGGER.info("Schedules received: {}", schedulesJson);
            Object schedulesDoc = Configuration.defaultConfiguration().jsonProvider().parse(schedulesJson);
            matches = read(schedulesDoc, MATCHES_PATH);
            matchIds = read(schedulesDoc, MATCH_IDS_PATH);
            teamNames = read(schedulesDoc, CLUB_NAMES_PATH);
            schedulesError = read(schedulesDoc, ERROR_PATH);
        } catch (Exception e) {
            return new Schedules(Collections.emptyList(),
                    Collections.emptyList(),
                    new Crests(null, null, null),
                    putErrorMessages(e.getMessage()));
        }

        try {
            String userBetsJson = getUserBets(userId, matchIds);
            Object userBetsDoc = Configuration.defaultConfiguration().jsonProvider().parse(userBetsJson);
            LOGGER.info("Bets received: {}", userBetsJson);
            userBets = read(userBetsDoc, BET_PAYLOAD_PATH);
            betsError = read(userBetsDoc, ERROR_PATH);
        } catch (Exception e) {
            betsError = e.getMessage();
        }
        return new Schedules(matches,
                userBets,
                new Crests(getClubBadges(teamNames), CREST_ADDRESS_PREFIX, FILE_FORMAT),
                putErrorMessages(schedulesError, betsError));
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

    private List<String> putErrorMessages(String... errors) {
        List<String> errorList = new ArrayList<>();
        for (String error: errors) {
            if (!"".equals(error)) {
                errorList.add(error);
            }
        }

        return errorList;
    }
}
