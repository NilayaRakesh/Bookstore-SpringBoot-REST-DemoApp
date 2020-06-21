package com.nr.bookstore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkuDto {
    private Long skuId;
    private Long CatalogItemId;
    private Integer quantity;
    private Double price;
}
