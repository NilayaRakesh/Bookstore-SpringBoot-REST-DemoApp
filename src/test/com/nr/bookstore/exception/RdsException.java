package com.nr.bookstore.exception;

import org.springframework.dao.DataAccessException;

public class RdsException extends DataAccessException {

    private static final String MESSAGE = "Test Rds Exception";

    public RdsException() {
        super(MESSAGE);
    }
}
