package hu.bets.apigateway.web.api;

import hu.bets.apigateway.model.users.FriendsUpdate;
import hu.bets.apigateway.model.users.User;
import hu.bets.apigateway.service.users.UsersService;
import hu.bets.apigateway.web.model.users.GetFriendsPayload;
import hu.bets.apigateway.web.model.users.PayloadWithToken;
import hu.bets.apigateway.web.model.users.UsersResponse;
import hu.bets.common.util.json.Json;
import hu.bets.common.util.json.JsonParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("aggregator/v1/users")
@Component
public class UsersResource {

    private static final Json JSON = new Json();
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersResource.class);
    private UsersService usersService;

    public UsersResource(UsersService usersService) {
        this.usersService = usersService;
    }

    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response register(String payload) {
        LOGGER.info("Register endpoint called with payload: {}", payload);
        try {
            User user = JSON.fromJson(payload, User.class);
            LOGGER.info("{} converted to User object: {}", payload, user);
            LOGGER.info("Calling users service to register the user.");
            usersService.registerUse(user);
            LOGGER.info("User successfully registered.");

            return Response.ok()
                    .entity(JSON.toJson(UsersResponse.success("Successfully registered user.", "empty_token")))
                    .build();
        } catch (JsonParsingException e) {
            return Response.status(400).entity("Invalid JSON request received").build();
        } catch (Exception e) {
            LOGGER.error("Exception caught while registering user. ", e);
            return Response.serverError()
                    .entity(JSON.toJson(UsersResponse.failure("Unable to register user.", "empty_token")))
                    .build();
        }
    }

    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response updateFriends(String payload) {
        LOGGER.info("Update friends endpoint called with payload: {}", payload);
        try {
            FriendsUpdate friendsUpdate = JSON.fromJson(payload, FriendsUpdate.class);
            LOGGER.info("{} converted to FriendsUpdate object: {}", payload, friendsUpdate);
            LOGGER.info("Calling users service to modify user list.");
            String response = usersService.updateFreindsList(friendsUpdate);
            List<User> newList = checkResponse(response);
            LOGGER.info("User list successfully modified. Resulting list is: {}", newList);

            return Response.ok()
                    .entity(JSON.toJson(UsersResponse.success(newList, "empty_token")))
                    .build();
        } catch (JsonParsingException e) {
            return Response.status(400).entity("Invalid JSON request received").build();
        }  catch (Exception e) {
            LOGGER.error("Exception caught while updating user list. ", e);
            return Response.serverError()
                    .entity(JSON.toJson(UsersResponse.failure("Unable to register user. " + e.getMessage(), "empty_token")))
                    .build();
        }
    }

    @Path("user-friends")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response getFriends(String payload) {
        LOGGER.info("Get friends endpoint called with payload: {}", payload);
        try {
            GetFriendsPayload getFriendsPayload = JSON.fromJson(payload, GetFriendsPayload.class);
            LOGGER.info("{} converted to GetFriendsPayload object: {}", payload, getFriendsPayload);
            LOGGER.info("Calling users service to get friends list.");
            String response = usersService.getFriends(getFriendsPayload.getUserId());
            List<User> newList = checkResponse(response);
            LOGGER.info("User list successfully retrieved. Resulting list is: {}", newList);

            return Response.ok().entity(JSON.toJson(UsersResponse.success(newList, "empty_token"))).build();
        } catch (JsonParsingException e) {
            return Response.status(400).entity("Invalid JSON request received").build();
        }  catch (Exception e) {
            LOGGER.error("Exception caught while retrieving user list. ", e);
            return Response.serverError()
                    .entity(JSON.toJson(UsersResponse.failure("Unable to retrieve user list. " + e.getMessage(), "empty_token")))
                    .build();
        }
    }

    private List<User> checkResponse(String response) {
        PayloadWithToken payloadWithToken = JSON.fromJson(response, PayloadWithToken.class);
        LOGGER.info("Response {} converted to object {}", response, payloadWithToken);

        if (payloadWithToken.getError() != null && !(payloadWithToken.getError().trim().equals(""))) {
            LOGGER.warn("User service returned an error: {}", payloadWithToken.getError());
            throw new UserServiceException(payloadWithToken.getError());
        }

        LOGGER.info("User service returned OK result.");
        return payloadWithToken.getPayload();
    }

    private class UserServiceException extends RuntimeException {
        UserServiceException(String error) {
            super(error);
        }
    }
}