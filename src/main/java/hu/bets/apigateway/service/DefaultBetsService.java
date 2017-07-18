package hu.bets.apigateway.service;

import hu.bets.apigateway.command.CommandFacade;
import hu.bets.apigateway.model.UserBet;

public class DefaultBetsService implements BetsService {

    private CommandFacade commandFacade;

    public DefaultBetsService(CommandFacade commandFacade) {
        this.commandFacade = commandFacade;
    }

    @Override
    public String sendBetsToBetService(UserBet payload) {
        return commandFacade.sendUserBets(payload).execute();
    }
}
