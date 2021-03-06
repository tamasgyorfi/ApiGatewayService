package hu.bets.apigateway.service.bets;

import hu.bets.apigateway.command.CommandFacade;
import hu.bets.apigateway.model.bets.UserBet;

public class DefaultBetsService implements BetsService {

    private CommandFacade commandFacade;

    public DefaultBetsService(CommandFacade commandFacade) {
        this.commandFacade = commandFacade;
    }

    @Override
    public String sendBetsToBetService(String userId, UserBet payload) {
        return commandFacade.getSendUserBetsCommand(userId, payload).execute();
    }
}
