package com.nr.bookstore.repository.elasticsearch;

import com.nr.bookstore.log.Logger;
import com.nr.bookstore.model.api.BookFilterRequest;
import com.nr.bookstore.model.api.PaginationRequest;
import com.nr.bookstore.model.elasticsearch.BookESDoc;
import com.nr.bookstore.util.JsonUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class CustomBookESDocRepository {

    private BookESDocRepository bookESDocRepository;

    private static final String ISBN_KEY = "isbn.keyword";
    private static final String AUTHOR_KEY = "author";
    private static final String TITLE_KEY = "title";

    private static final Logger LOGGER = new Logger(CustomBookESDocRepository.class);

    @Autowired
    public CustomBookESDocRepository(BookESDocRepository bookESDocRepository) {
        this.bookESDocRepository = bookESDocRepository;
    }


    @Async
    public BookESDoc save(BookESDoc bookESDoc) {
        LOGGER.info("inserting to ES: " + JsonUtil.toJsonString(bookESDoc));
        return bookESDocRepository.save(bookESDoc);
    }


    public Page<BookESDoc> filterBooks(BookFilterRequest bookFilterRequest, PaginationRequest paginationRequest) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (Objects.nonNull(paginationRequest)) {
            queryBuilder.withPageable(PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize()));
        }

        if (Objects.nonNull(bookFilterRequest.getIsbn())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ISBN_KEY, bookFilterRequest.getIsbn()));
        }

        if (Objects.nonNull(bookFilterRequest.getTitle())) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery(TITLE_KEY, getPartialMatchWildcard(bookFilterRequest.getTitle())));
        }

        if (Objects.nonNull(bookFilterRequest.getAuthor())) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery(AUTHOR_KEY, getPartialMatchWildcard(bookFilterRequest.getAuthor())));
        }

        queryBuilder.withQuery(boolQueryBuilder);
        NativeSearchQuery query = queryBuilder.build();
        LOGGER.debug("Book ES Search Query: " + query.getQuery());
        LOGGER.debug("Book ES Search Filters: " + query.getFilter());
        return bookESDocRepository.search(query);
    }


    private String getPartialMatchWildcard(String s) {
        return new String(new StringBuilder("*").append(s.toLowerCase()).append("*"));
    }
}
