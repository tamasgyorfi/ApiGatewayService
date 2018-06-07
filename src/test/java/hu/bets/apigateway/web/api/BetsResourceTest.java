package hu.bets.apigateway.web.api;

import com.google.common.collect.Lists;
import hu.bets.apigateway.model.bets.Bet;
import hu.bets.apigateway.model.bets.UserBet;
import hu.bets.apigateway.service.bets.BetsService;
import hu.bets.common.util.schema.SchemaValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BetsResourceTest {

    private static final String PAYLOAD = "{  \n" +
            "   \"userId\":\"user1\",\n" +
            "   \"bets\": [\n" +
            "  {\"competitionId\":\"CL\",\n" +
            "    \"matchId\":\"12\",\n" +
            "    \"matchDate\":\"2018\",\n" +
            "    \"homeTeamId\":\"Real Madrid\",\n" +
            "    \"awayTeamId\":\"Barcelona\",\n" +
            "    \"homeTeamGoals\":1,\n" +
            "    \"awayTeamGoals\":0},\n" +
            "  {\"competitionId\":\"EL\",\n" +
            "      \"matchId\":\"31\",\n" +
            "    \"matchDate\":\"2018\",\n" +
            "      \"homeTeamId\":\"Arsenal\",\n" +
            "      \"awayTeamId\":\"Dinamo Zagreb\",\n" +
            "      \"homeTeamGoals\":1,\n" +
            "      \"awayTeamGoals\":2}]\n" +
            "}";
    @Mock
    private BetsResource sut;
    @Mock
    private BetsService betsService;
    @Mock
    private SchemaValidator schemaValidatorMock;

    private UserBet userBet = new UserBet("user1",
            Lists.newArrayList(
                    new Bet("CL", "12", "2019","Real Madrid", "Barcelona", 1, 0),
                    new Bet("EL", "31", "2019","Arsenal", "Dinamo Zagreb", 1, 2)
            ));

    @Before
    public void before() {
        sut = new BetsResource(betsService) {
            {
                schemaValidator = schemaValidatorMock;
            }
        };
    }

    @Test
    public void userBetsEndpointShouldConvertPayloadAndDelegate() {
        sut.sendBets("userId", PAYLOAD);
        verify(betsService).sendBetsToBetService("userId", userBet);
    }

    @Test
    public void exceptionResultsInErrorPayload() {
        when(betsService.sendBetsToBetService("userId", userBet)).thenThrow(new IllegalArgumentException());
        Response response = sut.sendBets("userId", PAYLOAD);
        String result = (String) response.getEntity();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("{\"payload\":[],\"error\":\"Unable to send user bets to the Bets-Service.\",\"token\":\"token\"}", result);
    }
}