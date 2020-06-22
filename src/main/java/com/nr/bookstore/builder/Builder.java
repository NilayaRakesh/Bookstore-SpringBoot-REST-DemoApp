package com.nr.bookstore.builder;

@FunctionalInterface
public interface Builder<T> {

    T build();
}
