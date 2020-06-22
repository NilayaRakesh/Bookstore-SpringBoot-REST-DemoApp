package com.nr.bookstore.rest;

import lombok.Getter;

@Getter
public class PostRequest extends RestRequest {

    private Object body;

    private PostRequest(PostRequestBuilder postRequestBuilder) {
        super(postRequestBuilder);
        this.body = postRequestBuilder.body;
    }

    public static class PostRequestBuilder extends RestRequest.RestRequestBuilder<PostRequest> {

        private Object body;

        public PostRequestBuilder(String host) {
            super(host);
        }

        public PostRequestBuilder body(Object body) {
            this.body = body;
            return this;
        }

        @Override
        public PostRequest build() {
            return new PostRequest(this);
        }
    }
}
