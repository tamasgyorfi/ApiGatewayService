package hu.bets.apigateway.service.users;

import hu.bets.apigateway.command.CommandFacade;
import hu.bets.apigateway.model.users.FriendsUpdate;
import hu.bets.apigateway.model.users.User;

public class DefaultUsersService implements UsersService {

    private CommandFacade commandFacade;

    public DefaultUsersService(CommandFacade commandFacade) {
        this.commandFacade = commandFacade;
    }

    @Override
    public String updateFreindsList(FriendsUpdate update) {
        return commandFacade.getUpdateFriendsCommand(update).execute();
    }

    @Override
    public String getFriends(String userId) {
        return commandFacade.getUserFriendsCommand(userId).execute();
    }

    @Override
    public void registerUse(User user) {
        commandFacade.getUserRegisterCommand(user).execute();
    }

}
