package com.nr.bookstore.config;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    public static final int MAX_CONN_TOTAL = 100;
    public static final int MAX_CONN_PER_ROUTE = 20;
    public static final int MAX_CONN_MEDIA_COVERAGE = 10;

    private RestTemplateBuilder restTemplateBuilder;
    private String mediaCoverageHost;

    @Autowired
    public RestTemplateConfiguration(RestTemplateBuilder restTemplateBuilder,
                                     @Value("${host.mediaCoverage}") String mediaCoverageHost) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.mediaCoverageHost = mediaCoverageHost;
    }

    @Bean
    public RestTemplate customRestTemplate() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.setRequestFactory(customHttpRequestFactory());
        return restTemplate;
    }

    public HttpComponentsClientHttpRequestFactory customHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory customFactory = new HttpComponentsClientHttpRequestFactory();
//        customFactory.setConnectionRequestTimeout(3000);
//        customFactory.setReadTimeout(40000);
//        customFactory.setConnectTimeout(3000);
        customFactory.setHttpClient(closeableHttpClient());
        return customFactory;
    }

    @Bean
    public CloseableHttpClient closeableHttpClient() {
//        RequestConfig config = RequestConfig.custom()
//                .setConnectTimeout(3000)
//                .setConnectionRequestTimeout(3000)
//                .setSocketTimeout(180000).build();

        CloseableHttpClient client = HttpClientBuilder.create()
                .setMaxConnTotal(MAX_CONN_TOTAL)
                .setConnectionManager(getConnectionManager())
                .build();
        return client;
    }


    public PoolingHttpClientConnectionManager getConnectionManager() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(MAX_CONN_PER_ROUTE);
        HttpHost host = new HttpHost(mediaCoverageHost);
        connManager.setMaxPerRoute(new HttpRoute(host), MAX_CONN_MEDIA_COVERAGE);
        return connManager;
    }
}
