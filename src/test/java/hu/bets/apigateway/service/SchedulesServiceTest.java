package hu.bets.apigateway.service;

import com.google.gson.Gson;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SchedulesServiceTest {

    private static ClubBadgeResolverService badgeService = null;
    private SchedulesService sut = new SchedulesService(null, badgeService) {
        @Override
        protected String getUserBets(String userId, List<String> matchIds) {
            return "{\"error\":\"\",\"payload\":[{\"matchId\":\"match2\",\"homeTeamGoals\":1,\"awayTeamGoals\":0},{\"matchId\":\"match3\",\"homeTeamGoals\":1,\"awayTeamGoals\":0},{\"matchId\":\"match4\",\"homeTeamGoals\":1,\"awayTeamGoals\":0}]}";
        }

        @Override
        protected String getSchedules() {
            return schedulesResponse;
        }

    };

    private static String schedulesResponse = "";


    @BeforeClass
    public static void beforeClass() throws IOException, URISyntaxException {
        Path p = Paths.get(SchedulesServiceTest.class.getClassLoader().getResource("schedules.response.json").toURI());
        byte[] bytes = Files.readAllBytes(p);

        schedulesResponse = new String(bytes);

        badgeService = new ClubBadgeResolverService();
        badgeService.init();
    }

    @Test
    public void shouldCorrectlyGenerateResultingPayload() throws URISyntaxException, IOException {
        Path p = Paths.get(SchedulesServiceTest.class.getClassLoader().getResource("expectedAggregationResult.json").toURI());
        byte[] bytes = Files.readAllBytes(p);

        assertEquals(new String(bytes), new Gson().toJson(sut.getAggregatetResult("user1")));
    }
}
