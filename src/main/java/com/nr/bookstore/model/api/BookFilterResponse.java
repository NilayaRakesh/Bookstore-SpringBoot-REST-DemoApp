package com.nr.bookstore.model.api;

import com.nr.bookstore.model.dto.BookDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BookFilterResponse {
    private List<BookDto> books;
    private Long count;
}
