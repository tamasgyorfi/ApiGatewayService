package hu.bets.apigateway.command;

import com.google.gson.Gson;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.services.Services;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RetrieveUserBetsCommand extends HystrixCommand<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveSchedulesCommand.class);
    private static final String USER_BETS_PATH = "/bets/football/v1/userBets";
    private static final int TIMEOUT = 10_000;

    private ServiceResolverService serviceResolverService;
    private String userId;
    private List<String> matchIds;

    public RetrieveUserBetsCommand(ServiceResolverService serviceResolverService, String userId, List<String> matchIds) {
        super(HystrixCommandGroupKey.Factory.asKey("BETS"));
        this.serviceResolverService = serviceResolverService;
        this.userId = userId;
        this.matchIds = matchIds;
    }

    @Override
    protected String run() throws Exception {
        String fullEndpoint = getFullEndpoint();
        Optional<HttpPost> httpPost = makePost(fullEndpoint);
        if (httpPost.isPresent()) {
            LOGGER.info("Running http post to retrieve bets.");
            return runPost(httpPost.get());
        }

        return getFallback();
    }

    private String runPost(HttpPost httpPost) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .build();

        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();

        try {
            CloseableHttpResponse response = client.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());
            LOGGER.info("Successfully retrieved schedules from football-bets service.");
            return result;
        } catch (IOException e) {
            LOGGER.error("Unable to run http post. ", e);
        }

        return getFallback();
    }

    @Override
    protected String getFallback() {
        return String.format("{\"payload\": [], \"token\":\"%s\",\"error\": \"Unable to retrieve user bets.\"}", "security-token-to-be-filled");
    }

    private String getFullEndpoint() {
        String endpoint = serviceResolverService.resolve(Services.BETS);
        LOGGER.info("PATH is: {}", endpoint + USER_BETS_PATH);
        return endpoint + USER_BETS_PATH;
    }

    private Optional<HttpPost> makePost(String fullEndpoint) {
        try {
            HttpPost request = new HttpPost(fullEndpoint);
            HttpEntity entity = new StringEntity(new Gson().toJson(new UserBetsRequest(userId, matchIds, "token-to-be-filled")));
            request.setEntity(entity);
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            return Optional.of(request);
        } catch (Exception e) {
            LOGGER.info("Exception while trying to build post request to retrieve bets. {}", e);
        }

        return Optional.empty();
    }

    private static class UserBetsRequest {
        private String userId;
        private List<String> ids;
        private String token;

        public UserBetsRequest(String userId, List<String> ids, String token) {
            this.userId = userId;
            this.ids = ids;
            this.token = token;
        }

        public String getUserId() {
            return userId;
        }

        public String getToken() {
            return token;
        }
    }
}
