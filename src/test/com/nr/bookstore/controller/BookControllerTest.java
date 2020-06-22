package com.nr.bookstore.controller;

import com.nr.bookstore.constant.ErrorMessage;
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
    private static final String EMPTY_STRING = "";

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController cut;

    @Test
    public void testAddBookOk() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
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
    public void testAddBookNullIsbn() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
            addBookRequest.getBook().setIsbn(null);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.ISBN_REQUIRED, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookEmptyIsbn() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
            addBookRequest.getBook().setIsbn(EMPTY_STRING);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.ISBN_REQUIRED, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookNullTitle() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
            addBookRequest.getBook().setTitle(null);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.TITLE_REQUIRED, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookEmptyTitle() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
            addBookRequest.getBook().setTitle(EMPTY_STRING);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.TITLE_REQUIRED, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookNullAuthor() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
            addBookRequest.getBook().setAuthor(null);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.AUTHOR_REQUIRED, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookEmptyAuthor() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
            addBookRequest.getBook().setAuthor(EMPTY_STRING);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.AUTHOR_REQUIRED, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookNullPrice() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
            addBookRequest.getBook().setPrice(null);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.PRICE_REQUIRED, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookNullQuantity() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
            addBookRequest.getBook().setQuantity(null);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.QUANTITY_REQUIRED, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookInvalidPrice() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
            addBookRequest.getBook().setPrice(-1D);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.INVALID_PRICE, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookInvalidQuantity() {
        try {
            AddBookRequest addBookRequest = getValidAddBookRequest();
            addBookRequest.getBook().setQuantity(-1);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.INVALID_QUANTITY, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookEmptyBook() {
        try {
            AddBookRequest addBookRequest = new AddBookRequest(null);
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(addBookRequest);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.EMPTY_REQUEST, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookEmptyRequest() {
        try {
            ResponseEntity<RestResponse<AddBookResponse>> response = cut.addBook(null);
            fail();

        } catch (BadRequestException e){
            assertEquals(ErrorMessage.EMPTY_REQUEST, e.getMessage());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBookBadRequest() {
        try {
            BookDto requestBookDto = new BookDto();
            requestBookDto.setIsbn(DUMMY_ISBN);
            requestBookDto.setTitle(DUMMY_TITLE);
            requestBookDto.setAuthor(DUMMY_AUTHOR);
            requestBookDto.setPrice(DUMMY_PRICE);
            requestBookDto.setQuantity(DUMMY_QUANTITY);
            AddBookRequest addBookRequest = new AddBookRequest(requestBookDto);
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
            BookDto requestBookDto = new BookDto();
            requestBookDto.setIsbn(DUMMY_ISBN);
            requestBookDto.setTitle(DUMMY_TITLE);
            requestBookDto.setAuthor(DUMMY_AUTHOR);
            requestBookDto.setPrice(DUMMY_PRICE);
            requestBookDto.setQuantity(DUMMY_QUANTITY);
            AddBookRequest addBookRequest = new AddBookRequest(requestBookDto);
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

    private AddBookRequest getValidAddBookRequest() {
        BookDto requestBookDto = new BookDto();
        requestBookDto.setIsbn(DUMMY_ISBN);
        requestBookDto.setTitle(DUMMY_TITLE);
        requestBookDto.setAuthor(DUMMY_AUTHOR);
        requestBookDto.setPrice(DUMMY_PRICE);
        requestBookDto.setQuantity(DUMMY_QUANTITY);
        return new AddBookRequest(requestBookDto);
    }
}