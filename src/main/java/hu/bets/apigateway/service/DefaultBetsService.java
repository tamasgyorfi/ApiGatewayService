package hu.bets.apigateway.service;

import hu.bets.apigateway.command.CommandFacade;

public class DefaultBetsService implements BetsService {

    private CommandFacade commandFacade;

    public DefaultBetsService(CommandFacade commandFacade) {
        this.commandFacade = commandFacade;
    }

    @Override
    public String sendBetsToBetService(String payload) {
        return commandFacade.sendUserBets(payload).execute();
    }
}
