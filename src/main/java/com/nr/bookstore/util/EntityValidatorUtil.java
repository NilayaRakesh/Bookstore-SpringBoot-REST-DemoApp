package com.nr.bookstore.util;

public class EntityValidatorUtil {

    public static boolean isValidPrice(double price) {
        return price >= 0;
    }

    public static boolean isInvalidPrice(double price) {
        return !isValidPrice(price);
    }

    public static boolean isValidQuantity(int quantity) {
        return quantity >= 0;
    }

    public static boolean isInvalidQuantity(int quantity) {
        return !isValidQuantity(quantity);
    }
}
