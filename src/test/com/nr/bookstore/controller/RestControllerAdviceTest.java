package com.nr.bookstore.controller;

import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.ExternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.api.RestResponse;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class RestControllerAdviceTest extends TestCase {

    public static final String DUMMY_ERROR_MESSAGE = "Dummy Message";

    @InjectMocks
    private RestControllerAdvice cut;

    @Test
    public void testHandleBadRequest() {
        ResponseEntity<RestResponse> response = cut.handleBadRequest(new BadRequestException(DUMMY_ERROR_MESSAGE));
        testForErrorResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testHandleNotFound() {
        ResponseEntity<RestResponse> response = cut.handleNotFound(new NotFoundException(DUMMY_ERROR_MESSAGE));
        testForErrorResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @Test
    public void testHandleExternalException() {
        ResponseEntity<RestResponse> response = cut.handleExternalException(new ExternalException(DUMMY_ERROR_MESSAGE));
        testForErrorResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testHandleException() {
        ResponseEntity<RestResponse> response = cut.handleException(new Exception(DUMMY_ERROR_MESSAGE));
        testForErrorResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void testForErrorResponseEntity(ResponseEntity<RestResponse> response, HttpStatus httpStatus) {
        assertNotNull(response);
        assertEquals(httpStatus, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(httpStatus.value(), response.getBody().getCode());
        assertNotNull(response.getBody().getError());
        assertEquals(DUMMY_ERROR_MESSAGE, response.getBody().getError().getMessage());
    }

}