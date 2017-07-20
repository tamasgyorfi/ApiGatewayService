package hu.bets.apigateway.service;

import com.netflix.hystrix.HystrixCommand;
import hu.bets.apigateway.command.CommandFacade;
import hu.bets.apigateway.model.UserBet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBetsServiceTest {

    @Mock
    private CommandFacade commandFacade;
    @Mock
    private UserBet userBet;
    @Mock
    private HystrixCommand<String> command;
    private DefaultBetsService sut;

    @Before
    public void before() {
        sut = new DefaultBetsService(commandFacade);
    }

    @Test
    public void sendBetsToBetServiceShouldDelegateCallToCommandFacade() {
        when(commandFacade.sendUserBets(userBet)).thenReturn(command);
        sut.sendBetsToBetService(userBet);

        verify(commandFacade).sendUserBets(userBet);
    }
}