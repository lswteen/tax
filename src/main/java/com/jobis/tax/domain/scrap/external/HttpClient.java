package com.jobis.tax.domain.scrap.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HttpClient {
    private final ObjectMapper objectMapper;

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
    private static final int TIMEOUT = 20000;
    private static final String serverUrl = "https://codetest.3o3.co.kr/v2/scrap";

    public String sendPost(String userName, String regNo) throws IOException {
        CloseableHttpClient httpClient = createHttpClient();
        HttpPost httpPost = createHttpPost(serverUrl, userName, regNo);

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            } else {
                throw new IOException("Unexpected HTTP response status: " + httpResponse.getStatusLine().getStatusCode());
            }
        }
    }

    private CloseableHttpClient createHttpClient() {
        return HttpClients.custom()
                .setDefaultRequestConfig(createRequestConfig())
                .build();
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();
    }

    private HttpPost createHttpPost(String url, String userName, String regNo) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader(HttpHeaders.USER_AGENT, USER_AGENT);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpPost.setEntity(createJsonEntity(userName, regNo));

        return httpPost;
    }

    private HttpEntity createJsonEntity(String userName, String regNo) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("name", userName);
        map.put("regNo", regNo);

        String jsonValue = objectMapper.writeValueAsString(map);

        return new StringEntity(jsonValue, "utf-8");
    }
}
