package com.nr.bookstore.controller;

import com.nr.bookstore.constant.ErrorMessage;
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

    private static final Long DUMMY_SKU_ID = 1L;
    private static final Integer DUMMY_QUANTITY = 5;

    private CreatePurchaseRequest getValidCreatePurchaseRequest() {
        return new CreatePurchaseRequest(DUMMY_SKU_ID, DUMMY_QUANTITY);
    }

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController cut;

    @Test
    public void testCreatePurchase() {
        try {
            CreatePurchaseRequest createPurchaseRequest = getValidCreatePurchaseRequest();
            CreatePurchaseResponse dummyResponse = mock(CreatePurchaseResponse.class);
            doReturn(dummyResponse).when(purchaseService).createPurchase(createPurchaseRequest);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(createPurchaseRequest);

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
    public void testCreatePurchaseNullSkuId() {
        try {
            CreatePurchaseRequest createPurchaseRequest = getValidCreatePurchaseRequest();
            createPurchaseRequest.setSkuId(null);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(createPurchaseRequest);
            fail();
        } catch (BadRequestException e) {
            assertEquals(ErrorMessage.SKU_ID_REQUIRED, e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseNullQuantity() {
        try {
            CreatePurchaseRequest createPurchaseRequest = getValidCreatePurchaseRequest();
            createPurchaseRequest.setQuantity(null);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(createPurchaseRequest);
            fail();
        } catch (BadRequestException e) {
            assertEquals(ErrorMessage.QUANTITY_REQUIRED, e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseInvalidQuantity() {
        try {
            CreatePurchaseRequest createPurchaseRequest = getValidCreatePurchaseRequest();
            createPurchaseRequest.setQuantity(-1);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(createPurchaseRequest);
            fail();
        } catch (BadRequestException e) {
            assertEquals(ErrorMessage.INVALID_QUANTITY, e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseEmptyRequest() {
        try {
            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(null);
            fail();
        } catch (BadRequestException e) {
            assertEquals(ErrorMessage.EMPTY_REQUEST, e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseBadRequest() {
        try {
            CreatePurchaseRequest createPurchaseRequest = getValidCreatePurchaseRequest();
            doThrow(BadRequestException.class).when(purchaseService).createPurchase(createPurchaseRequest);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(createPurchaseRequest);
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
            CreatePurchaseRequest createPurchaseRequest = getValidCreatePurchaseRequest();
            doThrow(NotFoundException.class).when(purchaseService).createPurchase(createPurchaseRequest);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(createPurchaseRequest);
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
            CreatePurchaseRequest createPurchaseRequest = getValidCreatePurchaseRequest();
            doThrow(InternalException.class).when(purchaseService).createPurchase(createPurchaseRequest);

            ResponseEntity<RestResponse<CreatePurchaseResponse>> response = cut.createPurchase(createPurchaseRequest);
            fail();
        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
}