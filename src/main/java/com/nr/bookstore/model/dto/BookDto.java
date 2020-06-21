package com.nr.bookstore.model.dto;

import com.nr.bookstore.model.elasticsearch.BookESDoc;
import com.nr.bookstore.model.elasticsearch.SkuESDoc;
import com.nr.bookstore.model.rds.Book;
import com.nr.bookstore.model.rds.Sku;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private Long catalogItemId;
    private Long skuId;
    private String isbn;
    private String title;
    private String author;
    private Double price = 0D;
    private Integer quantity = 0;

    public static class Builder {

        public static BookDto fromRdsEntity(final Book book, final List<Sku> skus) {
            Sku sku = skus.get(0);
            return BookDto.builder()
                    .catalogItemId(book.getCatalogItemId())
                    .skuId(sku.getSkuId())
                    .isbn(book.getIsbn())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .price(sku.getPrice())
                    .quantity(sku.getQuantity())
                    .build();
        }

        public static BookDto fromEsDoc(final BookESDoc bookESDoc) {
            SkuESDoc skuESDoc = bookESDoc.getSkus().get(0);
            return BookDto.builder()
                    .catalogItemId(bookESDoc.getCatalogItemId())
                    .skuId(skuESDoc.getSkuId())
                    .isbn(bookESDoc.getIsbn())
                    .title(bookESDoc.getTitle())
                    .author(bookESDoc.getAuthor())
                    .price(skuESDoc.getPrice())
                    .build();
        }
    }
}
