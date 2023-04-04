package com.jobis.tax.domain.scrap.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.apache.http.HttpEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class HttpClient {
    private static final String USER_AGENT = "Mozila/5.0";

//    @Value("$openApi.szs.baseUri")
    private String serverUrl = "https://codetest.3o3.co.kr/v2/scrap";

    public String sendPost(String userName, String regNo) throws ClientProtocolException, IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(20000)
                        .setConnectTimeout(20000)
                        .setSocketTimeout(20000)
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                        .build())
                .build();

        //get
        HttpPost httpPost = new HttpPost(serverUrl);


        Map<String, String> map = new HashMap<String,String>();
        map.put("name", userName);
        map.put("regNo", regNo);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = objectMapper.writeValueAsString(map);
        HttpEntity httpEntity = new StringEntity(jsonValue, "utf-8");

        //agent
        httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.addHeader("Content-type", "application/json");
        httpPost.setEntity(httpEntity);

        //POST
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        String json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        return json;
    }
}
