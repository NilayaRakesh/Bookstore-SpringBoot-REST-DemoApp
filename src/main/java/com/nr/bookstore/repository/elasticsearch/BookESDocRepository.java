package com.nr.bookstore.repository.elasticsearch;

import com.nr.bookstore.model.elasticsearch.BookESDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookESDocRepository extends ElasticsearchRepository<BookESDoc, Long> {

}
