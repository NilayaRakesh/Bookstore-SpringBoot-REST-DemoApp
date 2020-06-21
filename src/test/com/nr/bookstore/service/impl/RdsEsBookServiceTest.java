package com.nr.bookstore.service.impl;

import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.exception.RdsException;
import com.nr.bookstore.manager.BookTransactionsManager;
import com.nr.bookstore.model.api.AddBookRequest;
import com.nr.bookstore.model.api.AddBookResponse;
import com.nr.bookstore.model.api.BookFilterRequest;
import com.nr.bookstore.model.api.FilterBookResponse;
import com.nr.bookstore.model.api.PaginationRequest;
import com.nr.bookstore.model.dto.BookDto;
import com.nr.bookstore.model.elasticsearch.BookESDoc;
import com.nr.bookstore.model.elasticsearch.SkuESDoc;
import com.nr.bookstore.model.rds.Book;
import com.nr.bookstore.model.rds.Sku;
import com.nr.bookstore.repository.elasticsearch.CustomBookESDocRepository;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RdsEsBookServiceTest extends TestCase {

    private static final String DUMMY_ISBN = "1001";
    private static final String DUMMY_TITLE = "Dummy Title";
    private static final String DUMMY_AUTHOR = "Dummy Author";
    private static final Double DUMMY_PRICE = 1000D;
    private static final Integer DUMMY_QUANTITY = 5;
    private static final Long DUMMY_CATALOG_ID = 1L;
    private static final Long DUMMY_SKU_ID = 1L;

    @Mock
    private BookTransactionsManager bookTransactionsManager;

    @Mock
    private CustomBookESDocRepository esRepository;

    @InjectMocks
    private RdsEsBookService cut;

    @Test
    public void testInsertBook() {
        try {
            AddBookRequest addBookRequest = new AddBookRequest();
            BookDto bookDto = new BookDto();
            bookDto.setIsbn(DUMMY_ISBN);
            bookDto.setTitle(DUMMY_TITLE);
            bookDto.setAuthor(DUMMY_AUTHOR);
            bookDto.setPrice(DUMMY_PRICE);
            bookDto.setQuantity(DUMMY_QUANTITY);
            addBookRequest.setBook(bookDto);

            Book book = new Book(DUMMY_ISBN, DUMMY_TITLE, DUMMY_AUTHOR);
            book.setCatalogItemId(DUMMY_CATALOG_ID);
            Sku dummySku = new Sku(book, DUMMY_PRICE, DUMMY_QUANTITY);
            dummySku.setSkuId(DUMMY_SKU_ID);
            book.setSkus(Collections.singletonList(dummySku));

            doReturn(book).when(bookTransactionsManager).insertBook(any());

            BookESDoc bookESDoc = mock(BookESDoc.class);
            doReturn(bookESDoc).when(esRepository).save(any());

            AddBookResponse addBookResponse = cut.insertBook(addBookRequest);

            assertNotNull(addBookResponse);
            assertNotNull(addBookResponse.getBookDto());
            BookDto addedBookDto = addBookResponse.getBookDto();
            assertEquals(addedBookDto.getCatalogItemId().longValue(), DUMMY_CATALOG_ID.longValue());
            assertEquals(addedBookDto.getSkuId().longValue(), DUMMY_SKU_ID.longValue());
            assertEquals(addedBookDto.getIsbn(), DUMMY_ISBN);
            assertEquals(addedBookDto.getTitle(), DUMMY_TITLE);
            assertEquals(addedBookDto.getAuthor(), DUMMY_AUTHOR);
            assertEquals(addedBookDto.getPrice(), DUMMY_PRICE, 0.01D);
            assertEquals(addedBookDto.getQuantity().intValue(), DUMMY_QUANTITY.intValue());
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testInsertConstraintViolation() {
        try {
            AddBookRequest addBookRequest = mock(AddBookRequest.class);

            doThrow(DataIntegrityViolationException.class).when(bookTransactionsManager).insertBook(any());

            AddBookResponse addBookResponse = cut.insertBook(addBookRequest);
            fail();
        } catch (BadRequestException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testInsertRdsError() {
        try {
            AddBookRequest addBookRequest = mock(AddBookRequest.class);

            doThrow(RdsException.class).when(bookTransactionsManager).insertBook(any());

            AddBookResponse addBookResponse = cut.insertBook(addBookRequest);
            fail();
        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testGetBookByIsbn() {
        try {
            Book book = new Book(DUMMY_ISBN, DUMMY_TITLE, DUMMY_AUTHOR);
            book.setCatalogItemId(DUMMY_CATALOG_ID);

            Sku sku = new Sku(book, DUMMY_PRICE, DUMMY_QUANTITY);
            sku.setSkuId(DUMMY_SKU_ID);
            book.setSkus(Collections.singletonList(sku));

            doReturn(book).when(bookTransactionsManager).getBookByIsbn(DUMMY_ISBN);

            BookDto bookDto = cut.getBookByIsbn(DUMMY_ISBN);

            assertNotNull(book);
            assertEquals(DUMMY_ISBN, bookDto.getIsbn());
            assertEquals(DUMMY_CATALOG_ID, bookDto.getCatalogItemId());
            assertEquals(DUMMY_SKU_ID, bookDto.getSkuId());
            assertEquals(DUMMY_TITLE, bookDto.getTitle());
            assertEquals(DUMMY_AUTHOR, bookDto.getAuthor());
            assertEquals(DUMMY_PRICE, bookDto.getPrice());
            assertEquals(DUMMY_QUANTITY, bookDto.getQuantity());

        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testGetBookByIsbnNotFound() {
        try {
            doThrow(NotFoundException.class).when(bookTransactionsManager).getBookByIsbn(DUMMY_ISBN);

            BookDto bookDto = cut.getBookByIsbn(DUMMY_ISBN);
            fail();

        } catch (NotFoundException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetBookByIsbnRdsError() {
        try {
            doThrow(RdsException.class).when(bookTransactionsManager).getBookByIsbn(DUMMY_ISBN);

            BookDto bookDto = cut.getBookByIsbn(DUMMY_ISBN);
            fail();

        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testFilterBooks() {
        try {
            BookFilterRequest dummyFilterRequest = mock(BookFilterRequest.class);
            PaginationRequest dummyPaginationRequest = mock(PaginationRequest.class);

            Integer totalCount = 1;

            BookESDoc dummyBookEsDoc = new BookESDoc();
            dummyBookEsDoc.setIsbn(DUMMY_ISBN);
            dummyBookEsDoc.setTitle(DUMMY_TITLE);
            dummyBookEsDoc.setAuthor(DUMMY_AUTHOR);
            dummyBookEsDoc.setCatalogItemId(DUMMY_CATALOG_ID);
            SkuESDoc dummySkuESDoc1 = new SkuESDoc();
            dummySkuESDoc1.setSkuId(DUMMY_SKU_ID);
            dummySkuESDoc1.setPrice(DUMMY_PRICE);
            dummyBookEsDoc.setSkus(Collections.singletonList(dummySkuESDoc1));
            Page<BookESDoc> dummyPage = new PageImpl<BookESDoc>(
                    Collections.singletonList(dummyBookEsDoc), PageRequest.of(1, 10), totalCount);

            doReturn(dummyPage).when(esRepository).filterBooks(dummyFilterRequest, dummyPaginationRequest);

            FilterBookResponse response = cut.filterBooks(dummyFilterRequest, dummyPaginationRequest);
            assertNotNull(response);
            assertNotNull(response.getBooks());
            assertEquals(totalCount, response.getTotalCount());
            assertEquals(1, response.getBooks().size());
            BookDto responseBookDto = response.getBooks().get(0);
            assertEquals(DUMMY_ISBN, responseBookDto.getIsbn());
            assertEquals(DUMMY_AUTHOR, responseBookDto.getAuthor());
            assertEquals(DUMMY_TITLE, responseBookDto.getTitle());
            assertEquals(DUMMY_SKU_ID, responseBookDto.getSkuId());
            assertEquals(DUMMY_CATALOG_ID, responseBookDto.getCatalogItemId());
            assertEquals(DUMMY_PRICE, responseBookDto.getPrice());

        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testFilterBooksEsError() {
        try {
            BookFilterRequest dummyFilterRequest = mock(BookFilterRequest.class);
            PaginationRequest dummyPaginationRequest = mock(PaginationRequest.class);
            doThrow(RuntimeException.class).when(esRepository).filterBooks(dummyFilterRequest, dummyPaginationRequest);

            FilterBookResponse response = cut.filterBooks(dummyFilterRequest, dummyPaginationRequest);
            fail();

        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
}