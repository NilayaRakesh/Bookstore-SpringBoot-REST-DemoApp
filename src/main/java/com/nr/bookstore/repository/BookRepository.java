package com.nr.bookstore.repository;

import com.nr.bookstore.model.rds.Book;

import java.util.List;

public interface BookRepository extends CatalogItemRepository<Book> {

    List<Book> findByIsbn(String isbn);
}
