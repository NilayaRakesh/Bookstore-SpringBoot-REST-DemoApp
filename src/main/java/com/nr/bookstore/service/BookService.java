package com.nr.bookstore.service;

import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.api.AddBookRequest;
import com.nr.bookstore.model.api.AddBookResponse;
import com.nr.bookstore.model.api.FilterBookResponse;
import com.nr.bookstore.model.dto.BookDto;
import com.nr.bookstore.model.api.BookFilterRequest;
import com.nr.bookstore.model.api.PaginationRequest;


public interface BookService {

    AddBookResponse insertBook(AddBookRequest addBookRequest) throws BadRequestException, InternalException;

    BookDto getBookByIsbn(String isbn) throws NotFoundException, InternalException;

    FilterBookResponse filterBooks(BookFilterRequest bookFilterRequest, PaginationRequest paginationRequest) throws InternalException;
}
