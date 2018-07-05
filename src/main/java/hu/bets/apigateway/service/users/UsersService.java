package hu.bets.apigateway.service.users;

import hu.bets.apigateway.model.users.FriendsUpdate;
import hu.bets.apigateway.model.users.User;

public interface UsersService {

    /**
     * Updates the user's friends list, by adding and removing connections as defined by the parameter update
     *
     * @param update - incapsulates the user's id, connections to be added and removed
     * @return the resulting JSON returned by the User Service.
     */
    String updateFreindsList(FriendsUpdate update);

    /**
     * Returns the friend list of the user identified by userId.
     *
     * @param userId - the user originating the retrieval.
     * @return the resulting JSON returned by the User Service.
     */
    String getFriends(String userId);

    /**
     * Registers the user defined by the parameter user.
     *
     * @param user - the user to be registered.
     */
    void registerUse(User user);
}
