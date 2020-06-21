package com.nr.bookstore.builder;

import com.nr.bookstore.model.api.RestResponseError;
import com.nr.bookstore.model.api.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder<T> implements Builder<ResponseEntity<RestResponse<T>>> {

    private HttpStatus httpStatus;
    private RestResponseError error;
    private T data;

    public ResponseEntityBuilder(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }


    public ResponseEntityBuilder<T> withData(T data) {
        this.data = data;
        return this;
    }


    public ResponseEntityBuilder<T> withError(Exception e) {
        this.error = new RestResponseError();
        this.error.setCode(this.httpStatus.value());
        this.error.setMessage(e.getMessage());
        return this;
    }


    @Override
    public ResponseEntity<RestResponse<T>> build() {
        RestResponse<T> restResponse = new RestResponse<>(httpStatus.value(), data, error);
        return new ResponseEntity<>(restResponse, httpStatus);
    }
}
