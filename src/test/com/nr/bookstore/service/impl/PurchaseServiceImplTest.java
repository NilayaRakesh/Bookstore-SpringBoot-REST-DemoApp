package com.nr.bookstore.service.impl;

import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.exception.RdsException;
import com.nr.bookstore.manager.PurchaseTransactionsManager;
import com.nr.bookstore.model.api.CreatePurchaseRequest;
import com.nr.bookstore.model.api.CreatePurchaseResponse;
import com.nr.bookstore.model.rds.CatalogItem;
import com.nr.bookstore.model.rds.Purchase;
import com.nr.bookstore.model.rds.Sku;
import com.nr.bookstore.service.SkuService;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;
import org.springframework.dao.DataIntegrityViolationException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseServiceImplTest extends TestCase {

    private static final Long DUMMY_SKU_ID = 1L;
    private static final Double DUMMY_PRICE = 1000D;
    private static final Integer DUMMY_QUANT = 5;
    private static final Integer DUMMY_TOTAL_QUANT = 10;

    @Mock
    private PurchaseTransactionsManager purchaseTransactionsManager;

    @Mock
    private SkuService skuService;

    @InjectMocks
    private PurchaseServiceImpl cut;

    private CreatePurchaseRequest createPurchaseRequest;

    @Before
    public void before() {
        createPurchaseRequest = new CreatePurchaseRequest(DUMMY_SKU_ID, DUMMY_QUANT);
    }

    @Test
    public void testCreatePurchaseOk() {
        try {
            Sku dummySku = new Sku(mock(CatalogItem.class), DUMMY_PRICE, DUMMY_TOTAL_QUANT);
            dummySku.setSkuId(DUMMY_SKU_ID);

            Purchase dummyPurchase = new Purchase(dummySku, DUMMY_PRICE, DUMMY_QUANT);
            doReturn(dummyPurchase).when(purchaseTransactionsManager).createPurchase(any());

            CreatePurchaseResponse response = cut.createPurchase(createPurchaseRequest);
            verify(skuService, times(0)).updateSku(anyLong(), any());
            assertNotNull(response);
            assertNotNull(response.getPurchase());
            assertEquals(DUMMY_PRICE, response.getPurchase().getPrice());
            assertEquals(DUMMY_QUANT, response.getPurchase().getQuantityBought());
            assertEquals(DUMMY_SKU_ID, response.getPurchase().getSkuId());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseQuantityDropZero() {
        try {
            Sku dummyUpdatedSku = new Sku(mock(CatalogItem.class), DUMMY_PRICE, 0);
            dummyUpdatedSku.setSkuId(DUMMY_SKU_ID);

            Purchase dummyPurchase = new Purchase(dummyUpdatedSku, DUMMY_PRICE, 1);
            doReturn(dummyPurchase).when(purchaseTransactionsManager).createPurchase(any());

            doReturn(mock(Sku.class)).when(skuService).updateSku(anyLong(), any());

            CreatePurchaseResponse response = cut.createPurchase(createPurchaseRequest);
            verify(skuService, times(1)).updateSku(anyLong(), any());
            assertNotNull(response);
            assertNotNull(response.getPurchase());
            assertEquals(DUMMY_PRICE, response.getPurchase().getPrice());
            assertEquals(DUMMY_QUANT, response.getPurchase().getQuantityBought());
            assertEquals(DUMMY_SKU_ID, response.getPurchase().getSkuId());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseNotFound() {
        try {
            doThrow(NotFoundException.class).when(purchaseTransactionsManager).createPurchase(any());

            CreatePurchaseResponse response = cut.createPurchase(createPurchaseRequest);
            fail();
        } catch (NotFoundException e) {
            assertTrue(true);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseBadRequest() {
        try {
            doThrow(BadRequestException.class).when(purchaseTransactionsManager).createPurchase(any());

            CreatePurchaseResponse response = cut.createPurchase(createPurchaseRequest);
            fail();
        } catch (BadRequestException e) {
            assertTrue(true);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseConstraintError() {
        try {
            doThrow(DataIntegrityViolationException.class).when(purchaseTransactionsManager).createPurchase(any());

            CreatePurchaseResponse response = cut.createPurchase(createPurchaseRequest);
            fail();
        } catch (BadRequestException e) {
            assertTrue(true);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseRdsError() {
        try {
            doThrow(RdsException.class).when(purchaseTransactionsManager).createPurchase(any());

            CreatePurchaseResponse response = cut.createPurchase(createPurchaseRequest);
            fail();
        } catch (InternalException e) {
            assertTrue(true);

        } catch (Exception e) {
            fail();
        }
    }
}