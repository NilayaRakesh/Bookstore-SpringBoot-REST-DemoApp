package com.nr.bookstore.model.api;

import lombok.Getter;

@Getter
public class RestResponse<T> {

    private int code;
    private T data;
    private RestResponseError error;

    public RestResponse(int code) {
        this.code = code;
    }

    public RestResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public RestResponse(int code, RestResponseError error) {
        this.code = code;
        this.error = error;
    }

    public RestResponse(int code, T data, RestResponseError error) {
        this.code = code;
        this.data = data;
        this.error = error;
    }


}
