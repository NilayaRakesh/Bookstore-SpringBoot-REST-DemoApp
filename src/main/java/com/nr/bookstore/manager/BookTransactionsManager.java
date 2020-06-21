package com.nr.bookstore.manager;

import com.nr.bookstore.constant.ErrorMessage;;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.dto.BookDto;
import com.nr.bookstore.model.dto.SkuDto;
import com.nr.bookstore.model.rds.Book;
import com.nr.bookstore.model.rds.Sku;
import com.nr.bookstore.repository.BookRepository;
import com.nr.bookstore.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class BookTransactionsManager {

    private BookRepository rdsRepository;
    private SkuTransactionsManager skuTransactionsManager;

    @Autowired
    public BookTransactionsManager(BookRepository rdsRepository, SkuTransactionsManager skuTransactionsManager) {
        this.rdsRepository = rdsRepository;
        this.skuTransactionsManager = skuTransactionsManager;
    }


    @Transactional(rollbackOn = Exception.class)
    public Book insertBook(BookDto bookDto) {
        Book book;
        List<Sku> skus;
        book = rdsRepository.save(
                new Book(bookDto.getIsbn(),
                        bookDto.getTitle(),
                        bookDto.getAuthor()));

        skus = skuTransactionsManager.saveSkus(
                Collections.singletonList(
                        SkuDto.builder()
                                .price(bookDto.getPrice())
                                .quantity(bookDto.getQuantity())
                                .build()),
                book);

        book.setSkus(skus);
        return book;
    }


    public Book getBookByIsbn(String isbn) throws NotFoundException {
        List<Book> books = rdsRepository.findByIsbn(isbn);
        if (CollectionUtils.isEmpty(books)) {
            throw new NotFoundException(ErrorMessage.BOOKS_NOT_FOUND);
        }

        Book book = books.get(0); // since there will be only one book per isbn
        return book;
    }
}
