package com.nr.bookstore.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestJson {
    private String field1 = "field1";
    private TestJsonInner field2 = new TestJsonInner();
}
