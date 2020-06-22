package com.nr.bookstore.model.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BookFilterRequest {

    private String isbn;
    private String author;
    private String title;
}
