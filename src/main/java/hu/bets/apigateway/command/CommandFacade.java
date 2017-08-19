package hu.bets.apigateway.command;

import com.netflix.hystrix.HystrixCommand;
import hu.bets.apigateway.command.bets.RetrieveUserBetsCommand;
import hu.bets.apigateway.command.bets.SendUserBetsCommand;
import hu.bets.apigateway.command.schedules.RetrieveSchedulesCommand;
import hu.bets.apigateway.command.users.RegisterUserCommand;
import hu.bets.apigateway.command.users.RetrieveFriendsCommand;
import hu.bets.apigateway.command.users.UpdateFriendsCommand;
import hu.bets.apigateway.model.bets.UserBet;
import hu.bets.apigateway.model.users.FriendsUpdate;
import hu.bets.apigateway.model.users.User;
import hu.bets.apigateway.service.ServiceResolverService;

import java.util.List;

public class CommandFacade {

    private ServiceResolverService resolverService;

    public CommandFacade(ServiceResolverService resolverService) {
        this.resolverService = resolverService;
    }

    public HystrixCommand<String> getRetrieveSchedulesCommand() {
        return new RetrieveSchedulesCommand(resolverService);
    }

    public HystrixCommand<String> getRetrieveBetsCommand(String userId, List<String> matchIds) {
        return new RetrieveUserBetsCommand(resolverService, userId, matchIds);
    }

    public HystrixCommand<String> getSendUserBetsCommand(UserBet payload) {
        return new SendUserBetsCommand(resolverService, payload);
    }

    public HystrixCommand<String> getUserRegisterCommand(User user) {
        return new RegisterUserCommand(resolverService, user);
    }

    public HystrixCommand<String> getUserFriendsCommand(String userId) {
        return new RetrieveFriendsCommand(resolverService, userId);
    }

    public HystrixCommand<String> getUpdateFriendsCommand(FriendsUpdate friendsUpdate) {
        return new UpdateFriendsCommand(resolverService, friendsUpdate);
    }

}
