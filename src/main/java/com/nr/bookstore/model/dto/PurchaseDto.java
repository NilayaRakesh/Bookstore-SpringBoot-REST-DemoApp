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
public class PurchaseDto {
    private Long purchaseId;
    private Long skuId;
    private Integer quantityBought;
    private Double price;
}
