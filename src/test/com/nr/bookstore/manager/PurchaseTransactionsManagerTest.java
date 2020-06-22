package com.nr.bookstore.manager;

import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.exception.RdsException;
import com.nr.bookstore.model.dto.PurchaseDto;
import com.nr.bookstore.model.rds.Purchase;
import com.nr.bookstore.model.rds.Sku;
import com.nr.bookstore.repository.PurchaseRepository;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseTransactionsManagerTest extends TestCase {

    private static final Long DUMMY_SKU_ID = 1L;
    private static final Long DUMMY_PURCHASE_ID = 2L;
    private static final Integer DUMMY_QUANT = 5;
    private static final Integer DUMMY_OVER_COUNT = 15;
    private static final Integer DUMMY_TOTAL_QUANT = 10;
    private static final Double DUMMY_PRICE = 1000D;


    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private SkuTransactionsManager skuTransactionsManager;

    @InjectMocks
    private PurchaseTransactionsManager cut;

    @Test
    public void testCreatePurchase() {
        try {
            PurchaseDto purchaseDto = PurchaseDto.builder()
                    .skuId(DUMMY_SKU_ID)
                    .quantityBought(DUMMY_QUANT)
                    .build();
            Sku dummySku = new Sku(null, null, DUMMY_TOTAL_QUANT);
            dummySku.setSkuId(DUMMY_SKU_ID);
            doReturn(dummySku).when(skuTransactionsManager).getSku(DUMMY_SKU_ID);

            Sku dummyUpdatedSku = new Sku(null, null, DUMMY_TOTAL_QUANT - DUMMY_QUANT);
            dummyUpdatedSku.setSkuId(DUMMY_SKU_ID);
            doReturn(dummyUpdatedSku).when(skuTransactionsManager).updateSku(eq(DUMMY_SKU_ID), any());

            Purchase dummyPurchase = new Purchase(dummySku, DUMMY_PRICE, DUMMY_QUANT);
            dummyPurchase.setPurchaseId(DUMMY_PURCHASE_ID);
            doReturn(dummyPurchase).when(purchaseRepository).save(any());

            Purchase response = cut.createPurchase(purchaseDto);
            assertNotNull(response);
            assertEquals(DUMMY_PURCHASE_ID, response.getPurchaseId());
            assertEquals(DUMMY_QUANT, response.getPurchasedQuantity());
            assertEquals(DUMMY_SKU_ID, response.getSku().getSkuId());
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    public void testCreatePurchaseSkuInsufficientQuantity() {
        try {
            PurchaseDto purchaseDto = PurchaseDto.builder()
                    .skuId(DUMMY_SKU_ID)
                    .quantityBought(DUMMY_OVER_COUNT)
                    .build();
            Sku dummySku = new Sku(null, null, DUMMY_TOTAL_QUANT);
            dummySku.setSkuId(DUMMY_SKU_ID);
            doReturn(dummySku).when(skuTransactionsManager).getSku(DUMMY_SKU_ID);

            Purchase response = cut.createPurchase(purchaseDto);
            fail();
        } catch (BadRequestException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCreatePurchaseSkuSkuNotFound() {
        try {
            PurchaseDto purchaseDto = PurchaseDto.builder()
                    .skuId(DUMMY_SKU_ID)
                    .quantityBought(DUMMY_OVER_COUNT)
                    .build();
            doThrow(NotFoundException.class).when(skuTransactionsManager).getSku(DUMMY_SKU_ID);
            Purchase response = cut.createPurchase(purchaseDto);
            fail();
        } catch (NotFoundException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testCreatePurchaseRdsErrorWhileGetSku() {
        try {
            PurchaseDto purchaseDto = PurchaseDto.builder()
                    .skuId(DUMMY_SKU_ID)
                    .quantityBought(DUMMY_QUANT)
                    .build();

            doThrow(RdsException.class).when(skuTransactionsManager).getSku(DUMMY_SKU_ID);

            Purchase response = cut.createPurchase(purchaseDto);
            fail();
        } catch (DataAccessException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testCreatePurchaseRdsErrorWhileUpdateSku() {
        try {
            PurchaseDto purchaseDto = PurchaseDto.builder()
                    .skuId(DUMMY_SKU_ID)
                    .quantityBought(DUMMY_QUANT)
                    .build();
            Sku dummySku = new Sku(null, null, DUMMY_TOTAL_QUANT);
            dummySku.setSkuId(DUMMY_SKU_ID);
            doReturn(dummySku).when(skuTransactionsManager).getSku(DUMMY_SKU_ID);

            doThrow(RdsException.class).when(skuTransactionsManager).updateSku(eq(DUMMY_SKU_ID), any());

            Purchase response = cut.createPurchase(purchaseDto);
            fail();
        } catch (DataAccessException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testCreatePurchaseRdsErrorWhileCreatePurchase() {
        try {
            PurchaseDto purchaseDto = PurchaseDto.builder()
                    .skuId(DUMMY_SKU_ID)
                    .quantityBought(DUMMY_QUANT)
                    .build();
            Sku dummySku = new Sku(null, null, DUMMY_TOTAL_QUANT);
            dummySku.setSkuId(DUMMY_SKU_ID);
            doReturn(dummySku).when(skuTransactionsManager).getSku(DUMMY_SKU_ID);

            Sku dummyUpdatedSku = new Sku(null, null, DUMMY_TOTAL_QUANT - DUMMY_QUANT);
            dummyUpdatedSku.setSkuId(DUMMY_SKU_ID);
            doReturn(dummyUpdatedSku).when(skuTransactionsManager).updateSku(eq(DUMMY_SKU_ID), any());

            doThrow(RdsException.class).when(purchaseRepository).save(any());

            Purchase response = cut.createPurchase(purchaseDto);
            fail();
        } catch (DataAccessException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }

    }
}