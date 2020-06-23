package com.nr.bookstore.rest;

import com.nr.bookstore.builder.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;


@Getter
class RestRequest {
    private String uriString;
    private HttpHeaders httpHeaders;

    RestRequest(RestRequestBuilder builder) {
        this.uriString = builder.uriComponentsBuilder.toUriString();
        this.httpHeaders = builder.headers;
    }

    public abstract static class RestRequestBuilder<T> implements Builder<T> {
        HttpHeaders headers;
        UriComponentsBuilder uriComponentsBuilder;

        RestRequestBuilder(String httpHost) {
            headers = new HttpHeaders();
            uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(httpHost);
        }

        public RestRequest.RestRequestBuilder appendUrlSegment(String segment) {
            uriComponentsBuilder.pathSegment(segment);
            return this;
        }

        public RestRequest.RestRequestBuilder param(String key, String value) {
            uriComponentsBuilder.queryParam(key, value);
            return this;
        }

        public RestRequest.RestRequestBuilder header(String key, String value) {
            headers.add(key, value);
            return this;
        }
    }
}
