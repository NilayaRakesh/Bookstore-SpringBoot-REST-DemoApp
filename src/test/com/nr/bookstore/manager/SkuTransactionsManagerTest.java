package com.nr.bookstore.manager;

import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.exception.RdsException;
import com.nr.bookstore.model.dto.SkuDto;
import com.nr.bookstore.model.rds.CatalogItem;
import com.nr.bookstore.model.rds.Sku;
import com.nr.bookstore.repository.SkuRepository;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SkuTransactionsManagerTest extends TestCase {

    private static final Long DUMMY_SKU_ID = 1L;
    private static final Long DUMMY_CATALOG_ID = 2L;
    private static final Integer DUMMY_QUANT = 5;
    private static final Double DUMMY_PRICE = 1000D;

    @Mock
    private SkuRepository skuRepository;

    @InjectMocks
    private SkuTransactionsManager cut;

    @Test
    public void testSaveSkus() {
        try {
            SkuDto dummySkuDto = new SkuDto(DUMMY_SKU_ID, DUMMY_CATALOG_ID, DUMMY_QUANT, DUMMY_PRICE);
            CatalogItem dummyCatalogItem = new CatalogItem();
            dummyCatalogItem.setCatalogItemId(DUMMY_CATALOG_ID);

            Sku dummySavedSku = new Sku(dummyCatalogItem, DUMMY_PRICE, DUMMY_QUANT);
            dummySavedSku.setSkuId(DUMMY_SKU_ID);
            Iterable<Sku> dummySavedSkus = Collections.singletonList(dummySavedSku);
            doReturn(dummySavedSkus).when(skuRepository).saveAll(anyIterable());

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
    public void testSaveRdsError() {
        try {
            SkuDto dummySkuDto = new SkuDto(DUMMY_SKU_ID, DUMMY_CATALOG_ID, DUMMY_QUANT, DUMMY_PRICE);
            CatalogItem dummyCatalogItem = new CatalogItem();
            dummyCatalogItem.setCatalogItemId(DUMMY_CATALOG_ID);

            doThrow(RdsException.class).when(skuRepository).saveAll(anyIterable());

            List<Sku> response = cut.saveSkus(Collections.singletonList(dummySkuDto), dummyCatalogItem);
            fail();

        } catch (DataAccessException e) {
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

            doReturn(Optional.of(dummySku)).when(skuRepository).findById(DUMMY_SKU_ID);

            dummySku.setPrice(priceToUpdate);
            dummySku.setQuantity(quantityToUpdate);
            doReturn(dummySku).when(skuRepository).save(any());

            Sku response = cut.updateSku(DUMMY_SKU_ID, dummySkuDto);
            assertNotNull(response);
            assertEquals(dummySku, response);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUpdateSkuSkuNotFound() {
        try {
            Double priceToUpdate = 1000D;
            Integer quantityToUpdate = 20;
            SkuDto dummySkuDto = SkuDto.builder().price(priceToUpdate).quantity(quantityToUpdate).build();

            doReturn(Optional.empty()).when(skuRepository).findById(DUMMY_SKU_ID);

            Sku response = cut.updateSku(DUMMY_SKU_ID, dummySkuDto);
            fail();

        } catch (NotFoundException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testUpdateSkuRdsErrorWhileFind() {
        try {
            Double priceToUpdate = 1000D;
            Integer quantityToUpdate = 20;
            SkuDto dummySkuDto = SkuDto.builder().price(priceToUpdate).quantity(quantityToUpdate).build();

            doThrow(RdsException.class).when(skuRepository).findById(DUMMY_SKU_ID);

            Sku response = cut.updateSku(DUMMY_SKU_ID, dummySkuDto);
            fail();

        } catch (DataAccessException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testUpdateSkuRdsErrorWhileSave() {
        try {
            Double priceToUpdate = 1000D;
            Integer quantityToUpdate = 20;
            SkuDto dummySkuDto = SkuDto.builder().price(priceToUpdate).quantity(quantityToUpdate).build();

            CatalogItem dummyCatalogItem = new CatalogItem();
            dummyCatalogItem.setCatalogItemId(DUMMY_CATALOG_ID);
            Sku dummySku = new Sku(dummyCatalogItem, DUMMY_PRICE, DUMMY_QUANT);
            dummySku.setSkuId(DUMMY_SKU_ID);

            doReturn(Optional.of(dummySku)).when(skuRepository).findById(DUMMY_SKU_ID);

            dummySku.setPrice(priceToUpdate);
            dummySku.setQuantity(quantityToUpdate);
            doThrow(RdsException.class).when(skuRepository).save(any());

            Sku response = cut.updateSku(DUMMY_SKU_ID, dummySkuDto);
            fail();

        } catch (DataAccessException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testGetSku() {
        try {
            Sku dummySku = mock(Sku.class);
            doReturn(Optional.of(dummySku)).when(skuRepository).findById(DUMMY_SKU_ID);

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
            doReturn(Optional.empty()).when(skuRepository).findById(DUMMY_SKU_ID);
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
            doThrow(RdsException.class).when(skuRepository).findById(DUMMY_SKU_ID);
            Sku sku = cut.getSku(DUMMY_SKU_ID);
            fail();

        } catch (DataAccessException e) {
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
            doReturn(dummySkus).when(skuRepository).findByCatalogItemCatalogItemId(DUMMY_CATALOG_ID);

            List<Sku> skus = cut.getSkusByCatalogItemId(DUMMY_CATALOG_ID);
            assertNotNull(skus);
            assertEquals(dummySkus, skus);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetSkusByCatalogItemIdRdsError() {
        try {
            doThrow(RdsException.class).when(skuRepository).findByCatalogItemCatalogItemId(DUMMY_CATALOG_ID);

            List<Sku> skus = cut.getSkusByCatalogItemId(DUMMY_CATALOG_ID);
            fail();

        } catch (DataAccessException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
}