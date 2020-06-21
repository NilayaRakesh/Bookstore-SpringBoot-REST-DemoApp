package com.nr.bookstore.controller;

import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.model.api.AddBookRequest;
import com.nr.bookstore.model.api.AddBookResponse;
import com.nr.bookstore.model.api.FilterBookResponse;
import com.nr.bookstore.model.api.RestResponse;
import com.nr.bookstore.model.dto.BookDto;
import com.nr.bookstore.service.BookService;
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
public class BookControllerTest extends TestCase {

    private static final String DUMMY_ISBN = "1001";
    private static final String DUMMY_TITLE = "Dummy Title";
    private static final String DUMMY_AUTHOR = "Dummy Author";
    private static final Double DUMMY_PRICE = 1000D;
    private static final Integer DUMMY_QUANTITY = 5;
    private static final Long DUMMY_CATALOG_ID = 1L;
    private static final Long DUMMY_SKU_ID = 1L;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController cut;

    @Test
    public void testAddBookOk() {
        try {
            AddBookRequest addBookRequest = mock(AddBookRequest.class);
            BookDto bookDto = new BookDto();
            bookDto.setCatalogItemId(DUMMY_CATALOG_ID);
            bookDto.setSkuId(DUMMY_SKU_ID);
            bookDto.setIsbn(DUMMY_ISBN);
            bookDto.setTitle(DUMMY_TITLE);
            bookDto.setAuthor(DUMMY_AUTHOR);
            bookDto.setPrice(DUMMY_PRICE);
            bookDto.setQuantity(DUMMY_QUANTITY);
            AddBookResponse addBookResponse = new AddBookResponse(bookDto);
            doReturn(addBookResponse).when(bookService).insertBook(any());

            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            assertNotNull(response);
            assertEquals(response.getStatusCode(), HttpStatus.CREATED);
            assertNotNull(response.getBody());
            assertEquals(response.getBody().getCode(), HttpStatus.CREATED.value());
            assertEquals(response.getBody().getData(), addBookResponse);
            assertNull(response.getBody().getError());

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookBadRequest() {
        try {
            AddBookRequest addBookRequest = mock(AddBookRequest.class);
            doThrow(BadRequestException.class).when(bookService).insertBook(any());

            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testAddBookInternalError() {
        try {
            AddBookRequest addBookRequest = mock(AddBookRequest.class);
            doThrow(InternalException.class).when(bookService).insertBook(any());

            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testFilterBooksOk() {
        try {
            FilterBookResponse filterBookResponse = mock(FilterBookResponse.class);
            doReturn(filterBookResponse).when(bookService).filterBooks(any(), any());

            ResponseEntity<RestResponse<FilterBookResponse>> response =
                    cut.filterBooks(DUMMY_ISBN, DUMMY_TITLE, DUMMY_AUTHOR, 0, 10);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(HttpStatus.OK.value(), response.getBody().getCode());
            assertEquals(filterBookResponse, response.getBody().getData());
            assertNull(response.getBody().getError());

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testFilterBooksInternalError() {
        try {
            doThrow(InternalException.class).when(bookService).filterBooks(any(), any());

            ResponseEntity<RestResponse<FilterBookResponse>> response =
                    cut.filterBooks(DUMMY_ISBN, DUMMY_TITLE, DUMMY_AUTHOR, 0, 10);
            fail();

        } catch (InternalException e) {
            assertTrue(true);

        } catch (Exception e) {
            fail();
        }
    }
}