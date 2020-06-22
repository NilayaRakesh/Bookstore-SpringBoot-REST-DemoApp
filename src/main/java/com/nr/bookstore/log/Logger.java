package com.nr.bookstore.log;


import org.slf4j.LoggerFactory;

public class Logger {

    private static final String MESSAGE_MARKER = "MESSAGE: ";
    private static final String EXCEPTION_MESSAGE_MARKER = "EXCEPTION MESSAGE: ";
    private static final String SEPARATOR = ", ";

    private org.slf4j.Logger logger;

    public Logger(Class clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable throwable) {
        StringBuilder finalMessage = new StringBuilder(MESSAGE_MARKER)
                .append(message)
                .append(SEPARATOR).append(EXCEPTION_MESSAGE_MARKER)
                .append(throwable.getMessage());
        logger.error(new String(finalMessage), throwable);
    }
}
