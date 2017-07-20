package hu.bets.apigateway.command;

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
import java.util.Optional;

abstract class CommandBase extends HystrixCommand<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendUserBetsCommand.class);
    private static final int TIMEOUT = 10_000;
    private final ServiceResolverService resolverService;


    CommandBase(HystrixCommandGroupKey group, ServiceResolverService resolverService) {
        super(group);
        this.resolverService = resolverService;
    }

    String getFullEndpoint(Services service, String path) {
        String endpoint = resolverService.resolve(service);
        LOGGER.info("PATH is: {}", endpoint + path);
        return endpoint + path;
    }

    Optional<HttpPost> makePost(String fullEndpoint, String payload) {
        try {
            HttpPost request = new HttpPost(fullEndpoint);
            HttpEntity entity = new StringEntity(payload);
            request.setEntity(entity);
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            return Optional.of(request);
        } catch (Exception e) {
            LOGGER.info("Exception while trying to build post request for path:{} and payload:{} ", fullEndpoint, payload, e);
        }

        return Optional.empty();
    }

    String runPost(HttpPost httpPost) {
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
            LOGGER.info("Successfully run post request.");
            return result;
        } catch (IOException e) {
            LOGGER.error("Unable to run http post. ", e);
        }

        return getFallback();
    }

}
