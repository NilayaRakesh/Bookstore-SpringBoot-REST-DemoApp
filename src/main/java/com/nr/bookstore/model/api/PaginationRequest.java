package com.nr.bookstore.model.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {
    private Integer page;
    private Integer size;
}
