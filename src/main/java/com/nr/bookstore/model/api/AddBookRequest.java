package com.nr.bookstore.model.api;

import com.nr.bookstore.model.dto.BookDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBookRequest {
    private BookDto book;
}
