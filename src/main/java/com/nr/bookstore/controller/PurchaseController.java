package com.nr.bookstore.controller;

import com.nr.bookstore.builder.ResponseEntityBuilder;
import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.api.CreatePurchaseRequest;
import com.nr.bookstore.model.api.CreatePurchaseResponse;
import com.nr.bookstore.model.api.RestResponse;
import com.nr.bookstore.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseController {

    private PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/purchases")
    public ResponseEntity<RestResponse<CreatePurchaseResponse>> createPurchase(@RequestBody CreatePurchaseRequest createPurchaseRequest)
            throws NotFoundException, BadRequestException, InternalException {

        CreatePurchaseResponse createPurchaseResponse = purchaseService.createPurchase(createPurchaseRequest);
        return new ResponseEntityBuilder<CreatePurchaseResponse>(HttpStatus.CREATED)
                .withData(createPurchaseResponse)
                .build();
    }
}
