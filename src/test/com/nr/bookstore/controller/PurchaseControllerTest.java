package com.nr.bookstore.controller;

import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.api.CreatePurchaseRequest;
import com.nr.bookstore.model.api.CreatePurchaseResponse;
import com.nr.bookstore.model.api.RestResponse;
import com.nr.bookstore.service.PurchaseService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseControllerTest extends TestCase {

    public static final CreatePurchaseRequest DUMMY_REQUEST = mock(CreatePurchaseRequest.class);

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController cut;

    @Test
    public void testCreatePurchase() {
        try {
            CreatePurchaseResponse dummyResponse = mock(CreatePurchaseResponse.class);
            doReturn(dummyResponse).when(purchaseService).createPurchase(DUMMY_REQUEST);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(DUMMY_REQUEST);

            assertNotNull(response);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(HttpStatus.CREATED.value(), response.getBody().getCode());
            assertNull(response.getBody().getError());
            assertEquals(dummyResponse, response.getBody().getData());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseBadRequest() {
        try {
            doThrow(BadRequestException.class).when(purchaseService).createPurchase(DUMMY_REQUEST);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(DUMMY_REQUEST);
            fail();
        } catch (BadRequestException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseNotFound() {
        try {
            doThrow(NotFoundException.class).when(purchaseService).createPurchase(DUMMY_REQUEST);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(DUMMY_REQUEST);
            fail();
        } catch (NotFoundException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseInternalError() {
        try {
            doThrow(InternalException.class).when(purchaseService).createPurchase(DUMMY_REQUEST);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(DUMMY_REQUEST);
            fail();
        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
}