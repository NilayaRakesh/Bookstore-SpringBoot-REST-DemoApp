package com.nr.bookstore.service.impl;

import com.nr.bookstore.constant.ErrorMessage;
import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.log.Logger;
import com.nr.bookstore.manager.BookTransactionsManager;
import com.nr.bookstore.model.api.AddBookRequest;
import com.nr.bookstore.model.api.AddBookResponse;
import com.nr.bookstore.model.api.FilterBookResponse;
import com.nr.bookstore.model.elasticsearch.SkuESDoc;
import com.nr.bookstore.model.rds.Book;
import com.nr.bookstore.model.dto.BookDto;
import com.nr.bookstore.model.api.BookFilterRequest;
import com.nr.bookstore.model.api.PaginationRequest;
import com.nr.bookstore.model.elasticsearch.BookESDoc;
import com.nr.bookstore.model.rds.Sku;
import com.nr.bookstore.repository.elasticsearch.CustomBookESDocRepository;
import com.nr.bookstore.service.BookService;
import com.nr.bookstore.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RdsEsBookService implements BookService {

    private CustomBookESDocRepository esRepository;
    private BookTransactionsManager bookTransactionsManager;

    private static final Logger LOGGER = new Logger(RdsEsBookService.class);

    @Autowired
    public RdsEsBookService(CustomBookESDocRepository esRepository, BookTransactionsManager bookTransactionsManager) {
        this.esRepository = esRepository;
        this.bookTransactionsManager = bookTransactionsManager;
    }

    @Override
    public AddBookResponse insertBook(AddBookRequest addBookRequest) throws BadRequestException, InternalException {
        Book book;
        try {
            book = bookTransactionsManager.insertBook(addBookRequest.getBook());

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Constraint error while inserting book", e);
            throw new BadRequestException(ErrorMessage.CONSTRAINT_VIOLATED);

        } catch (DataAccessException e) {
            LOGGER.error("Exception while inserting book", e);
            throw new InternalException(ErrorMessage.DB_ERROR);
        }

        List<Sku> skus = book.getSkus();
        esRepository.save(buildBookESDoc(book, skus));
        return new AddBookResponse(BookDto.Builder.fromRdsEntity(book, skus));
    }


    @Override
    public BookDto getBookByIsbn(String isbn) throws NotFoundException, InternalException {
        Book book;
        try {
            book = bookTransactionsManager.getBookByIsbn(isbn);

        } catch (DataAccessException e) {
            LOGGER.error("Exception while fetching book", e);
            throw new InternalException(ErrorMessage.DB_ERROR);
        }

        List<Sku> skus = book.getSkus();
        return BookDto.Builder.fromRdsEntity(book, skus);
    }


    @Override
    public FilterBookResponse filterBooks(BookFilterRequest bookFilterRequest, PaginationRequest paginationRequest) throws InternalException {
        Page<BookESDoc> searchPage;
        try {
            searchPage = esRepository.filterBooks(bookFilterRequest, paginationRequest);
        } catch (Exception e) {
            LOGGER.error("Exception while finding books in ES", e);
            throw new InternalException(ErrorMessage.ES_ERROR);
        }

        LOGGER.debug("ES Search results: " + JsonUtil.toJsonString(searchPage));

        List<BookDto> books = searchPage.get()
                .map(BookDto.Builder::fromEsDoc)
                .collect(Collectors.toList());
        return new FilterBookResponse(books, searchPage.getNumberOfElements());
    }


    private BookESDoc buildBookESDoc(final Book book, final List<Sku> skus) {
        BookESDoc bookESDoc = new BookESDoc();
        bookESDoc.setCatalogItemId(book.getCatalogItemId());
        bookESDoc.setCreatedAt(book.getCreatedAt());
        bookESDoc.setUpdatedAt(book.getUpdatedAt());
        bookESDoc.setIsbn(book.getIsbn());
        bookESDoc.setAuthor(book.getAuthor());
        bookESDoc.setTitle(book.getTitle());

        List<SkuESDoc> skuESDocs = skus.stream().map(this::buildSkuEsDoc).collect(Collectors.toList());
        bookESDoc.setSkus(skuESDocs);

        return bookESDoc;
    }


    private SkuESDoc buildSkuEsDoc(final Sku sku) {
        SkuESDoc skuESDoc = new SkuESDoc();
        skuESDoc.setSkuId(sku.getSkuId());
        skuESDoc.setPrice(sku.getPrice());
        skuESDoc.setCreatedAt(sku.getCreatedAt());
        skuESDoc.setUpdatedAt(sku.getUpdatedAt());
        return skuESDoc;
    }
}
