package hu.bets.apigateway.service;

import com.google.gson.Gson;
import com.netflix.hystrix.HystrixCommand;
import hu.bets.apigateway.command.CommandFacade;
import hu.bets.apigateway.service.schedules.ClubBadgeResolverService;
import hu.bets.apigateway.service.schedules.DefaultSchedulesService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSchedulesServiceTest {

    private static final String BETS_RESPONSE = "{\"error\":\"some\",\"payload\":[{\"matchId\":\"match2\",\"homeTeamGoals\":1,\"awayTeamGoals\":0},{\"matchId\":\"match3\",\"homeTeamGoals\":1,\"awayTeamGoals\":0},{\"matchId\":\"match4\",\"homeTeamGoals\":1,\"awayTeamGoals\":0}]}";

    private static ClubBadgeResolverService badgeService = null;
    @Mock
    private CommandFacade commandFacade;
    @Mock
    private HystrixCommand<String> schedulesCommand;
    @Mock
    private HystrixCommand<String> betsCommand;
    private DefaultSchedulesService sut;

    @Before
    public void before() {
        sut = new DefaultSchedulesService(commandFacade, badgeService);

        when(commandFacade.getRetrieveSchedulesCommand()).thenReturn(schedulesCommand);
        when(commandFacade.getRetrieveBetsCommand(eq("user1"), any(List.class))).thenReturn(betsCommand);

        when(schedulesCommand.execute()).thenReturn(schedulesResponse);
        when(betsCommand.execute()).thenReturn(BETS_RESPONSE);
    }

    private static String schedulesResponse = "";


    @BeforeClass
    public static void beforeClass() throws IOException, URISyntaxException {
        Path p = Paths.get(DefaultSchedulesServiceTest.class.getClassLoader().getResource("schedules.response.json").toURI());
        byte[] bytes = Files.readAllBytes(p);

        schedulesResponse = new String(bytes);

        badgeService = new ClubBadgeResolverService();
        badgeService.init();
    }

    @Test
    public void shouldCorrectlyGenerateResultingPayload() throws URISyntaxException, IOException {
        Path p = Paths.get(DefaultSchedulesServiceTest.class.getClassLoader().getResource("expectedAggregationResult.json").toURI());
        byte[] bytes = Files.readAllBytes(p);

        assertEquals(new String(bytes), new Gson().toJson(sut.getAggregatedResult("user1")));
    }

    @Test
    public void shouldReturnEmptyResultWheSchedulesAreUnreachable() throws URISyntaxException, IOException {

        when(schedulesCommand.execute()).thenThrow(new IllegalArgumentException("error"));

        Path p = Paths.get(DefaultSchedulesServiceTest.class.getClassLoader().getResource("expectedAggregationResult.json").toURI());
        byte[] bytes = Files.readAllBytes(p);

        assertEquals("{\"schedules\":[],\"bets\":[],\"crests\":{},\"errors\":[\"error\"]}", new Gson().toJson(sut.getAggregatedResult("user1")));
    }

}
