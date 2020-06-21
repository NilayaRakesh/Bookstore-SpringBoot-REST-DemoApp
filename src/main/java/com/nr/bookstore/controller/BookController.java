package com.nr.bookstore.controller;

import com.nr.bookstore.builder.ResponseEntityBuilder;
import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.model.api.AddBookRequest;
import com.nr.bookstore.model.api.AddBookResponse;
import com.nr.bookstore.model.api.FilterBookResponse;
import com.nr.bookstore.model.api.PaginationRequest;
import com.nr.bookstore.model.api.BookFilterRequest;
import com.nr.bookstore.model.api.RestResponse;
import com.nr.bookstore.service.BookService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @PostMapping("/books")
    public ResponseEntity<RestResponse<AddBookResponse>> addBook(@RequestBody AddBookRequest addBookRequest)
            throws BadRequestException, InternalException {

        AddBookResponse addBookResponse = bookService.insertBook(addBookRequest);
        return new ResponseEntityBuilder<AddBookResponse>(HttpStatus.CREATED)
                .withData(addBookResponse)
                .build();
    }


    @GetMapping("/books")
    public ResponseEntity<RestResponse<FilterBookResponse>> filterBooks(@RequestParam(required = false) String isbn,
                                                                        @RequestParam(required = false) String title,
                                                                        @RequestParam(required = false) String author,
                                                                        @RequestParam Integer pageNumber,
                                                                        @RequestParam Integer pageSize)
            throws InternalException {

        BookFilterRequest bookFilterRequest = BookFilterRequest.builder()
                .isbn(isbn)
                .author(author)
                .title(title).build();

        PaginationRequest paginationRequest = new PaginationRequest(pageNumber, pageSize);
        FilterBookResponse filterBookResponse = bookService.filterBooks(bookFilterRequest, paginationRequest);
        return new ResponseEntityBuilder<FilterBookResponse>(HttpStatus.OK)
                .withData(filterBookResponse)
                .build();
    }
}
