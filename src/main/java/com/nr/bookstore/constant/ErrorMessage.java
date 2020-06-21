package com.nr.bookstore.constant;

public class ErrorMessage {

    public static final String SKU_NOT_FOUND = "Catalog not found";
    public static final String BOOKS_NOT_FOUND = "Books not found";
    public static final String ISBN_EXISTS = "Book with given isbn already exists";
    public static final String SKU_CONSTRAINT_VIOLATED = "Sku constraint violated";
    public static final String INSUFFICIENT_QUANTITY = "Not enough stocks available";
    public static final String CONSTRAINT_VIOLATED = "Constraint violated while interacting with database";
    public static final String DB_ERROR = "Error while interacting with database";
    public static final String ES_ERROR = "Error while interacting with ES";
}
