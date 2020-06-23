package com.nr.bookstore.service.impl;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.nr.bookstore.exception.ExternalException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.log.Logger;
import com.nr.bookstore.manager.MediaCoverageManager;
import com.nr.bookstore.model.dto.BookDto;
import com.nr.bookstore.model.external.MediaCoverage;
import com.nr.bookstore.model.dto.MediaCoverageDto;
import com.nr.bookstore.service.BookService;
import com.nr.bookstore.service.MediaCoverageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeICodeMediaCoverageService implements MediaCoverageService {

    private BookService bookService;
    private MediaCoverageManager mediaCoverageManager;


    private static final Logger LOGGER = new Logger(TypeICodeMediaCoverageService.class);

    @Autowired
    public TypeICodeMediaCoverageService(BookService bookService, MediaCoverageManager mediaCoverageManager) {
        this.bookService = bookService;
        this.mediaCoverageManager = mediaCoverageManager;
    }


    @Override
    public List<MediaCoverageDto> fetchMediaCoverageForIsbn(String isbn)
            throws NotFoundException, InternalException, ExternalException {

        List<MediaCoverage> allMediaCoverages;
        try {
            allMediaCoverages = mediaCoverageManager.getAllMediaCoverages();

        } catch (RestClientException | HystrixRuntimeException e) {
            LOGGER.error("Exception while calling for media coverage", e);
            throw new ExternalException(e.getMessage());
        }

        BookDto book = bookService.getBookByIsbn(isbn);
        return allMediaCoverages
                .stream()
                .filter(mediaCoverage -> isMediaCoverageAboutBook(mediaCoverage, book))
                .map(x -> new MediaCoverageDto(x.getTitle()))
                .collect(Collectors.toList());
    }


    private boolean isMediaCoverageAboutBook(MediaCoverage mediaCoverage, BookDto book) {
        return StringUtils.containsIgnoreCase(mediaCoverage.getTitle(), book.getTitle())
                || StringUtils.containsIgnoreCase(mediaCoverage.getBody(), book.getTitle());
    }
}
