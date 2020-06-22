package com.nr.bookstore.controller;

import com.nr.bookstore.builder.ResponseEntityBuilder;
import com.nr.bookstore.constant.ErrorMessage;
import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.model.api.AddBookRequest;
import com.nr.bookstore.model.api.AddBookResponse;
import com.nr.bookstore.model.api.FilterBookResponse;
import com.nr.bookstore.model.api.PaginationRequest;
import com.nr.bookstore.model.api.BookFilterRequest;
import com.nr.bookstore.model.api.RestResponse;
import com.nr.bookstore.model.dto.BookDto;
import com.nr.bookstore.service.BookService;
import com.nr.bookstore.util.EntityValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


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

        validate(addBookRequest);
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


    private void validate(AddBookRequest addBookRequest) throws BadRequestException {
        if (Objects.isNull(addBookRequest)) {
            throw new BadRequestException(ErrorMessage.EMPTY_REQUEST);
        }
        BookDto bookDto = addBookRequest.getBook();
        if (Objects.isNull(bookDto)) {
            throw new BadRequestException(ErrorMessage.EMPTY_REQUEST);
        }
        if (StringUtils.isBlank(bookDto.getIsbn())) {
            throw new BadRequestException(ErrorMessage.ISBN_REQUIRED);
        }
        if (StringUtils.isBlank(bookDto.getTitle())) {
            throw new BadRequestException(ErrorMessage.TITLE_REQUIRED);
        }
        if (StringUtils.isBlank(bookDto.getAuthor())) {
            throw new BadRequestException(ErrorMessage.AUTHOR_REQUIRED);
        }
        if (Objects.isNull(bookDto.getPrice())) {
            throw new BadRequestException(ErrorMessage.PRICE_REQUIRED);
        }
        if (EntityValidatorUtil.isInvalidPrice(bookDto.getPrice())) {
            throw new BadRequestException(ErrorMessage.INVALID_PRICE);
        }
        if (Objects.isNull(bookDto.getQuantity())) {
            throw new BadRequestException(ErrorMessage.QUANTITY_REQUIRED);
        }
        if (EntityValidatorUtil.isInvalidQuantity(bookDto.getQuantity())) {
            throw new BadRequestException(ErrorMessage.INVALID_QUANTITY);
        }
    }
}
