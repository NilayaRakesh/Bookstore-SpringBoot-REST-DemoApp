package com.nr.bookstore.model.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Getter
@Setter
public class SkuESDoc {

    @Field(type = FieldType.Long)
    private Long skuId;

    @Field(type = FieldType.Date)
    private Date createdAt;

    @Field(type = FieldType.Date)
    private Date updatedAt;

    @Field(type = FieldType.Double)
    private Double price;
}
