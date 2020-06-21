package com.nr.bookstore.model.api;

import com.nr.bookstore.model.dto.PurchaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePurchaseResponse {
    private PurchaseDto purchase;
}
