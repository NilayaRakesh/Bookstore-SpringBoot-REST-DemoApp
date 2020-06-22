package com.nr.bookstore.rest;

public class GetRequest extends RestRequest {

    private GetRequest(GetRequestBuilder getRequestBuilder) {
        super(getRequestBuilder);
    }

    public static class GetRequestBuilder extends RestRequest.RestRequestBuilder<GetRequest> {

        public GetRequestBuilder(String host) {
            super(host);
        }

        @Override
        public GetRequest build() {
            return new GetRequest(this);
        }
    }
}
