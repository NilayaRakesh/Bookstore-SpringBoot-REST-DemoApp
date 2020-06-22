package com.nr.bookstore.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class TestJsonInner {
    private List<Integer> innerField1 = Arrays.asList(1, 2, 3);
}
