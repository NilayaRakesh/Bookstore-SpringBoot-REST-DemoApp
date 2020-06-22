package com.nr.bookstore.exception;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
