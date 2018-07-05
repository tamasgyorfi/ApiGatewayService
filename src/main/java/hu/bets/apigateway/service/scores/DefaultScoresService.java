package hu.bets.apigateway.service.scores;

import hu.bets.apigateway.command.CommandFacade;
import hu.bets.apigateway.model.scores.Toplist;

public class DefaultScoresService implements ScoresService {

    private CommandFacade commandFacade;

    public DefaultScoresService(CommandFacade commandFacade) {
        this.commandFacade = commandFacade;
    }


    @Override
    public Toplist getToplist(String userId) {
        return commandFacade.getToplistCommand(userId).execute();
    }
}
