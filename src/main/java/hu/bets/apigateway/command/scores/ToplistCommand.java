package hu.bets.apigateway.command.scores;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.command.CommandBase;
import hu.bets.apigateway.command.users.RetrieveFriendsCommand;
import hu.bets.apigateway.model.scores.Toplist;
import hu.bets.apigateway.model.scores.ToplistEntry;
import hu.bets.apigateway.model.users.User;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.util.json.Json;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ToplistCommand extends CommandBase<Toplist> {
    private static final Json JSON = new Json();
    private ServiceResolverService resolverService;
    private final String userId;

    public ToplistCommand(ServiceResolverService resolverService, String userId) {
        super(HystrixCommandGroupKey.Factory.asKey("USER_TOPLIST"), resolverService);
        this.resolverService = resolverService;
        this.userId = userId;
    }

    @Override
    protected Toplist run() {
        List<User> friends = JSON.fromJson(new RetrieveFriendsCommand(resolverService, userId).execute(), Friends.class).friends;
        List<String> userIds = friends.stream().map(User::getId).collect(Collectors.toList());

        List<ToplistEntry> entries = JSON.fromJson(new UserScoresCommand(resolverService, userIds).execute(), Entries.class).entries;

        return new Toplist(friends, entries);
    }

    @Override
    protected Toplist getFallback() {
        return new Toplist(Collections.emptyList(), Collections.emptyList());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Friends {
        List<User> friends;

        @JsonCreator
        public Friends(@JsonProperty("payload") List<User> friends) {
            this.friends = friends;
        }
    }

    private static class Entries {
        private List<ToplistEntry> entries;
        private String token;

        @JsonCreator
        public Entries(@JsonProperty("entries") List<ToplistEntry> entries, @JsonProperty("token") String token) {
            this.entries = entries;
            this.token = token;
        }
    }

}
