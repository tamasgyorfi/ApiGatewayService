package hu.bets.apigateway.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import hu.bets.apigateway.service.ServiceResolverService;
import hu.bets.common.services.Services;
import org.apache.http.HttpEntity;
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
import java.util.Optional;

public class RetrieveSchedulesCommand extends HystrixCommand<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveSchedulesCommand.class);
    private static final String SCHEDULES_PATH = "/matches/football/v1/schedules";
    private static final int TIMEOUT = 10_000;

    private ServiceResolverService serviceResolverService;

    public RetrieveSchedulesCommand(ServiceResolverService serviceResolverService) {
        super(HystrixCommandGroupKey.Factory.asKey("SCHEDULES"));
        this.serviceResolverService = serviceResolverService;
    }

    @Override
    protected String run() throws Exception {
        String fullEndpoint = getFullEndpoint();
        Optional<HttpPost> httpPost = makePost(fullEndpoint);
        if (httpPost.isPresent()) {
            LOGGER.info("Running http post to retrieve schedules.");
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
            LOGGER.info("Successfully retrieved schedules from football-matches service.");
            return result;
        } catch (IOException e) {
            LOGGER.error("Unable to run http post. ", e);
        }

        return getFallback();
    }

    @Override
    protected String getFallback() {
        return String.format("{\"matches\": [], \"token\":\"%s\",\"error\": \"Unable to retrieve schedules.\"}", "security-token-to-be-filled");
    }

    private String getFullEndpoint() {
        String endpoint = serviceResolverService.resolve(Services.MATCHES);
        return endpoint + SCHEDULES_PATH;
    }

    private Optional<HttpPost> makePost(String fullEndpoint) {
        try {
            HttpPost request = new HttpPost(fullEndpoint);
            HttpEntity entity = new StringEntity("\"token\":\"\"");
            request.setEntity(entity);
            return Optional.of(request);
        } catch (Exception e) {
            LOGGER.info("Exception while trying to build post request to retrieve schedules. {}", e);
        }

        return Optional.empty();
    }

}
