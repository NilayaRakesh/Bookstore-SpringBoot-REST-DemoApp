package com.nr.bookstore.service.impl;

import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.exception.RdsException;
import com.nr.bookstore.manager.SkuTransactionsManager;
import com.nr.bookstore.model.dto.SkuDto;
import com.nr.bookstore.model.rds.CatalogItem;
import com.nr.bookstore.model.rds.Sku;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SkuServiceImplTest extends TestCase {

    @Mock
    private SkuTransactionsManager skuTransactionsManager;

    @InjectMocks
    private SkuServiceImpl cut;

    private static final Long DUMMY_SKU_ID = 1L;
    private static final Long DUMMY_CATALOG_ID = 1L;
    private static final Integer DUMMY_QUANT = 5;
    private static final Double DUMMY_PRICE = 1000D;

    @Test
    public void testSaveSkus() {
        try {
            SkuDto dummySkuDto = new SkuDto(DUMMY_SKU_ID, DUMMY_CATALOG_ID, DUMMY_QUANT, DUMMY_PRICE);

            CatalogItem dummyCatalogItem = new CatalogItem();
            dummyCatalogItem.setCatalogItemId(DUMMY_CATALOG_ID);
            Sku dummySavedSku = new Sku(dummyCatalogItem, DUMMY_PRICE, DUMMY_QUANT);
            dummySavedSku.setSkuId(DUMMY_SKU_ID);
            List<Sku> dummySavedSkus = Collections.singletonList(dummySavedSku);
            doReturn(dummySavedSkus).when(skuTransactionsManager).saveSkus(anyList(), any());

            List<Sku> response = cut.saveSkus(Collections.singletonList(dummySkuDto), dummyCatalogItem);
            assertTrue(Objects.nonNull(response) && response.size() == 1);
            Sku responseSku = response.get(0);
            assertEquals(responseSku.getSkuId(), DUMMY_SKU_ID);
            assertEquals(responseSku.getPrice(), DUMMY_PRICE);
            assertEquals(responseSku.getQuantity(), DUMMY_QUANT);
            assertNotNull(responseSku.getCatalogItem());
            assertEquals(responseSku.getCatalogItem().getCatalogItemId(), DUMMY_CATALOG_ID);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testSaveSkusConstraintViolation() {
        try {
            SkuDto dummySkuDto = new SkuDto(DUMMY_SKU_ID, DUMMY_CATALOG_ID, DUMMY_QUANT, DUMMY_PRICE);

            CatalogItem dummyCatalogItem = new CatalogItem();
            dummyCatalogItem.setCatalogItemId(DUMMY_CATALOG_ID);
            doThrow(DataIntegrityViolationException.class).when(skuTransactionsManager).saveSkus(anyList(), any());

            List<Sku> response = cut.saveSkus(Collections.singletonList(dummySkuDto), dummyCatalogItem);
            fail();

        } catch (BadRequestException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testSaveSkusRdsError() {
        try {
            SkuDto dummySkuDto = new SkuDto(DUMMY_SKU_ID, DUMMY_CATALOG_ID, DUMMY_QUANT, DUMMY_PRICE);

            CatalogItem dummyCatalogItem = new CatalogItem();
            dummyCatalogItem.setCatalogItemId(DUMMY_CATALOG_ID);
            doThrow(RdsException.class).when(skuTransactionsManager).saveSkus(anyList(), any());

            List<Sku> response = cut.saveSkus(Collections.singletonList(dummySkuDto), dummyCatalogItem);
            fail();

        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testUpdateSku() {
        try {
            Double priceToUpdate = 1000D;
            Integer quantityToUpdate = 20;
            SkuDto dummySkuDto = SkuDto.builder().price(priceToUpdate).quantity(quantityToUpdate).build();

            CatalogItem dummyCatalogItem = new CatalogItem();
            dummyCatalogItem.setCatalogItemId(DUMMY_CATALOG_ID);
            Sku dummySku = new Sku(dummyCatalogItem, DUMMY_PRICE, DUMMY_QUANT);
            dummySku.setSkuId(DUMMY_SKU_ID);

            dummySku.setPrice(priceToUpdate);
            dummySku.setQuantity(quantityToUpdate);
            doReturn(dummySku).when(skuTransactionsManager).updateSku(any(), any());

            Sku response = cut.updateSku(DUMMY_SKU_ID, dummySkuDto);
            assertNotNull(response);
            assertEquals(priceToUpdate, response.getPrice());
            assertEquals(quantityToUpdate, response.getQuantity());
            assertEquals(DUMMY_SKU_ID, response.getSkuId());

        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testUpdateSkuNotFound() {
        try {
            Double priceToUpdate = 1000D;
            Integer quantityToUpdate = 20;
            SkuDto dummySkuDto = SkuDto.builder().price(priceToUpdate).quantity(quantityToUpdate).build();

            doThrow(NotFoundException.class).when(skuTransactionsManager).updateSku(any(), any());

            Sku response = cut.updateSku(DUMMY_SKU_ID, dummySkuDto);
            fail();

        } catch (NotFoundException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testUpdateSkuConstraintViolation() {
        try {
            Double priceToUpdate = 1000D;
            Integer quantityToUpdate = 20;
            SkuDto dummySkuDto = SkuDto.builder().price(priceToUpdate).quantity(quantityToUpdate).build();

            doThrow(DataIntegrityViolationException.class).when(skuTransactionsManager).updateSku(any(), any());

            Sku response = cut.updateSku(DUMMY_SKU_ID, dummySkuDto);
            fail();

        } catch (BadRequestException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUpdateSkuRdsError() {
        try {
            Double priceToUpdate = 1000D;
            Integer quantityToUpdate = 20;
            SkuDto dummySkuDto = SkuDto.builder().price(priceToUpdate).quantity(quantityToUpdate).build();

            doThrow(RdsException.class).when(skuTransactionsManager).updateSku(any(), any());

            Sku response = cut.updateSku(DUMMY_SKU_ID, dummySkuDto);
            fail();

        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testGetSku() {
        try {
            Sku dummySku = mock(Sku.class);
            doReturn(dummySku).when(skuTransactionsManager).getSku(DUMMY_SKU_ID);
            Sku sku = cut.getSku(DUMMY_SKU_ID);
            assertNotNull(sku);
            assertEquals(dummySku, sku);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetSkuNotFound() {
        try {
            doThrow(NotFoundException.class).when(skuTransactionsManager).getSku(DUMMY_SKU_ID);
            Sku sku = cut.getSku(DUMMY_SKU_ID);
            fail();
        } catch (NotFoundException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testGetSkuRdsError() {
        try {
            doThrow(RdsException.class).when(skuTransactionsManager).getSku(DUMMY_SKU_ID);
            Sku sku = cut.getSku(DUMMY_SKU_ID);
            fail();
        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testGetSkusByCatalogItemId() {
        try {
            Sku dummySku1 = mock(Sku.class);
            Sku dummySku2 = mock(Sku.class);
            List<Sku> dummySkus = Arrays.asList(dummySku1, dummySku2);
            doReturn(dummySkus).when(skuTransactionsManager).getSkusByCatalogItemId(DUMMY_CATALOG_ID);

            List<Sku> skus = cut.getSkusByCatalogItemId(DUMMY_SKU_ID);
            assertNotNull(skus);
            assertEquals(dummySkus, skus);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetSkusByCatalogItemIdRdsError() {
        try {
            doThrow(RdsException.class).when(skuTransactionsManager).getSkusByCatalogItemId(DUMMY_CATALOG_ID);

            List<Sku> skus = cut.getSkusByCatalogItemId(DUMMY_SKU_ID);

        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
}