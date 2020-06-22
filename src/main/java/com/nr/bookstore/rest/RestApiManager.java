package com.nr.bookstore.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nr.bookstore.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestApiManager {

    private RestTemplate restTemplate;

    @Autowired
    public RestApiManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public <T> T get(GetRequest getRequest, Class<T> responseClass) {
        ResponseEntity<T> responseEntity = restExchange(
                getRequest.getUriString(),
                HttpMethod.GET,
                new HttpEntity<>(getRequest.getHttpHeaders()),
                responseClass);
        return responseEntity.getBody();
    }


    public <T> T post(PostRequest postRequest, Class<T> responseClass) {
        String jsonBodyString;
        jsonBodyString = JsonUtil.toJsonString(postRequest.getBody());

        ResponseEntity<T> responseEntity = restExchange(
                postRequest.getUriString(),
                HttpMethod.POST,
                new HttpEntity<Object>(jsonBodyString, postRequest.getHttpHeaders()),
                responseClass);
        return responseEntity.getBody();
    }


    private <T> ResponseEntity<T> restExchange(String url,
                                               HttpMethod httpMethod,
                                               HttpEntity httpEntity,
                                               Class<T> responseClass) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, responseClass);
        return responseEntity;
    }
}
