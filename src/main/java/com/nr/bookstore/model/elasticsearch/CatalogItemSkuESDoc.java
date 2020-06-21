package com.nr.bookstore.model.elasticsearch;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public abstract class CatalogItemSkuESDoc {

    @Id
    private Long catalogItemId;

    @Field(type = FieldType.Date)
    private Date createdAt;

    @Field(type = FieldType.Date)
    private Date updatedAt;

    @Field
    private List<SkuESDoc> skus;
}
