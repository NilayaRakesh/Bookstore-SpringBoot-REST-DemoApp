package com.nr.bookstore.constant;

public class ErrorMessage {

    public static final String SKU_NOT_FOUND = "Catalog not found";
    public static final String BOOKS_NOT_FOUND = "Books not found";
    public static final String INSUFFICIENT_QUANTITY = "Not enough stocks available";
    public static final String CONSTRAINT_VIOLATED = "Constraint violated while interacting with database";
    public static final String DB_ERROR = "Error while interacting with database";
    public static final String ES_ERROR = "Error while interacting with ES";

    public static final String EMPTY_REQUEST = "Request can not be empty";
    public static final String INVALID_PRICE = "Price must be greater than or equal to zero";
    public static final String INVALID_QUANTITY = "Quantity must be greater than or equal to zero";
    public static final String ISBN_REQUIRED = "Field isbn is requuired";
    public static final String TITLE_REQUIRED = "Field title is required";
    public static final String AUTHOR_REQUIRED = "Field author is required";
    public static final String PRICE_REQUIRED = "Field price is required";
    public static final String QUANTITY_REQUIRED = "Field quantity is required";
    public static final String SKU_ID_REQUIRED = "Field sku id is required";
}
