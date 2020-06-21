package com.nr.bookstore.model.elasticsearch;

import com.nr.bookstore.constant.ElasticSearchConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = ElasticSearchConstant.BOOK_INDEX_NAME, type = ElasticSearchConstant.DOC_TYPE)
public class BookESDoc extends CatalogItemSkuESDoc {

    @Field(type = FieldType.Keyword)
    private String isbn;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String author;
}
