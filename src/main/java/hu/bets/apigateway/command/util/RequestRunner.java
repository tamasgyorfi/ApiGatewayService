package hu.bets.apigateway.command.util;

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

public class RequestRunner {

    private static final int TIMEOUT = 10_000;
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestRunner.class);

    public Optional<String> runRequest(String fullEndpoint, String payload) {
        Optional<HttpPost> httpPost = makePost(fullEndpoint, payload);
        if (httpPost.isPresent()) {
            return runPost(httpPost.get());
        }

        return Optional.empty();
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

    Optional<String> runPost(HttpPost httpPost) {
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
            return Optional.ofNullable(result);
        } catch (IOException e) {
            LOGGER.error("Unable to run http post. ", e);
        }

        return Optional.empty();
    }
}