package com.nr.bookstore.manager;

import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.exception.RdsException;
import com.nr.bookstore.model.dto.BookDto;
import com.nr.bookstore.model.rds.Book;
import com.nr.bookstore.model.rds.Sku;
import com.nr.bookstore.repository.BookRepository;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class BookTransactionsManagerTest extends TestCase {

    private static final String DUMMY_ISBN = "1001";
    private static final String DUMMY_TITLE = "Dummy Title";
    private static final String DUMMY_AUTHOR = "Dummy Author";
    private static final Double DUMMY_PRICE = 1000D;
    private static final Integer DUMMY_QUANTITY = 5;
    private static final Long DUMMY_CATALOG_ID = 1L;
    private static final Long DUMMY_SKU_ID = 1L;

    @Mock
    private BookRepository rdsRepository;

    @Mock
    private SkuTransactionsManager skuTransactionsManager;

    @InjectMocks
    private BookTransactionsManager cut;


    @Test
    public void testInsertBook() {
        try {
            BookDto bookDto = new BookDto();
            bookDto.setIsbn(DUMMY_ISBN);
            bookDto.setTitle(DUMMY_TITLE);
            bookDto.setAuthor(DUMMY_AUTHOR);
            bookDto.setPrice(DUMMY_PRICE);
            bookDto.setQuantity(DUMMY_QUANTITY);
            Book book = new Book(DUMMY_ISBN, DUMMY_TITLE, DUMMY_AUTHOR);
            book.setCatalogItemId(DUMMY_CATALOG_ID);
            doReturn(book).when(rdsRepository).save(any());

            Sku sku = new Sku(book, DUMMY_PRICE, DUMMY_QUANTITY);
            sku.setSkuId(DUMMY_SKU_ID);
            doReturn(Collections.singletonList(sku)).when(skuTransactionsManager).saveSkus(any(), any());

            Book response = cut.insertBook(bookDto);
            assertNotNull(response);
            assertEquals(DUMMY_CATALOG_ID, book.getCatalogItemId());
            assertEquals(DUMMY_ISBN, book.getIsbn());
            assertEquals(DUMMY_TITLE, book.getTitle());
            assertEquals(DUMMY_AUTHOR, book.getAuthor());
            List<Sku> responseSkus = response.getSkus();
            assertEquals(1, responseSkus.size());
            Sku responseSku = responseSkus.get(0);
            assertEquals(DUMMY_SKU_ID, responseSku.getSkuId());
            assertEquals(DUMMY_QUANTITY, responseSku.getQuantity());
            assertEquals(DUMMY_PRICE, responseSku.getPrice());
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testInsertBookRdsErrorWhileSaveSku() {
        try {
            BookDto bookDto = new BookDto();
            bookDto.setIsbn(DUMMY_ISBN);
            bookDto.setTitle(DUMMY_TITLE);
            bookDto.setAuthor(DUMMY_AUTHOR);
            bookDto.setPrice(DUMMY_PRICE);
            bookDto.setQuantity(DUMMY_QUANTITY);
            Book book = new Book(DUMMY_ISBN, DUMMY_TITLE, DUMMY_AUTHOR);
            book.setCatalogItemId(DUMMY_CATALOG_ID);
            doReturn(book).when(rdsRepository).save(any());

            doThrow(RdsException.class).when(skuTransactionsManager).saveSkus(any(), any());

            Book response = cut.insertBook(bookDto);
            fail();
        } catch (DataAccessException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testInsertBookRdsErrorWhileSaveBook() {
        try {
            BookDto bookDto = new BookDto();
            bookDto.setIsbn(DUMMY_ISBN);
            bookDto.setTitle(DUMMY_TITLE);
            bookDto.setAuthor(DUMMY_AUTHOR);
            bookDto.setPrice(DUMMY_PRICE);
            bookDto.setQuantity(DUMMY_QUANTITY);
            Book book = new Book(DUMMY_ISBN, DUMMY_TITLE, DUMMY_AUTHOR);
            book.setCatalogItemId(DUMMY_CATALOG_ID);
            doThrow(RdsException.class).when(rdsRepository).save(any());

            Book response = cut.insertBook(bookDto);
            fail();
        } catch (DataAccessException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetBookByIsbn() {
        try {
            Book book = new Book();
            book.setCatalogItemId(DUMMY_CATALOG_ID);
            book.setIsbn(DUMMY_ISBN);

            Sku sku = new Sku();
            sku.setCatalogItem(book);
            sku.setSkuId(DUMMY_SKU_ID);
            book.setSkus(Collections.singletonList(sku));

            doReturn(Collections.singletonList(book)).when(rdsRepository).findByIsbn(any());

            Book response = cut.getBookByIsbn(DUMMY_ISBN);

            assertNotNull(book);
            assertEquals(DUMMY_ISBN, response.getIsbn());
            assertEquals(DUMMY_CATALOG_ID, response.getCatalogItemId());
            assertEquals(1, response.getSkus().size());
            assertNotNull(response.getSkus().get(0));
            assertEquals(DUMMY_SKU_ID, response.getSkus().get(0).getSkuId());

        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetBookByIsbnNotFound() {
        try {
            doReturn(Collections.emptyList()).when(rdsRepository).findByIsbn(DUMMY_ISBN);
            Book response = cut.getBookByIsbn(DUMMY_ISBN);
            fail();

        } catch (NotFoundException e) {
            assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetBookByIsbnRdsError() {
        try {
            doThrow(RdsException.class).when(rdsRepository).findByIsbn(DUMMY_ISBN);
            Book response = cut.getBookByIsbn(DUMMY_ISBN);
            fail();

        } catch (DataAccessException e) {
            assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}