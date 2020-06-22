package com.nr.bookstore.model.api;

import com.nr.bookstore.model.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddBookRequest {
    private BookDto book;
}
