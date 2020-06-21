package com.nr.bookstore.controller;

import com.nr.bookstore.builder.ResponseEntityBuilder;
import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.ExternalException;
import com.nr.bookstore.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity handleBadRequest(BadRequestException e) {
        return new ResponseEntityBuilder<>(HttpStatus.BAD_REQUEST)
                .withError(e)
                .build();
    }


    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity handleNotFound(NotFoundException e) {
        return new ResponseEntityBuilder<>(HttpStatus.NOT_FOUND)
                .withError(e)
                .build();
    }


    @ExceptionHandler(value = {ExternalException.class})
    public ResponseEntity handleExternalException(ExternalException e) {
        return new ResponseEntityBuilder<>(HttpStatus.INTERNAL_SERVER_ERROR)
                .withError(e)
                .build();
    }


    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity handleException(Exception e) {
        return new ResponseEntityBuilder<>(HttpStatus.INTERNAL_SERVER_ERROR)
                .withError(e)
                .build();
    }
}
