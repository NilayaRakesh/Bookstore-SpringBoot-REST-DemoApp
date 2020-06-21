package com.nr.bookstore.model.api;

import com.nr.bookstore.model.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterBookResponse {
    private List<BookDto> books;
    private Integer totalCount;
}
