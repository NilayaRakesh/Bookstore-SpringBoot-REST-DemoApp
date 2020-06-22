package com.nr.bookstore.controller;

import com.nr.bookstore.builder.ResponseEntityBuilder;
import com.nr.bookstore.constant.ErrorMessage;
import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.api.CreatePurchaseRequest;
import com.nr.bookstore.model.api.CreatePurchaseResponse;
import com.nr.bookstore.model.api.RestResponse;
import com.nr.bookstore.service.PurchaseService;
import com.nr.bookstore.util.EntityValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

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

        validate(createPurchaseRequest);
        CreatePurchaseResponse createPurchaseResponse = purchaseService.createPurchase(createPurchaseRequest);
        return new ResponseEntityBuilder<CreatePurchaseResponse>(HttpStatus.CREATED)
                .withData(createPurchaseResponse)
                .build();
    }

    private void validate(CreatePurchaseRequest createPurchaseRequest) throws BadRequestException {
        if (Objects.isNull(createPurchaseRequest)) {
            throw new BadRequestException(ErrorMessage.EMPTY_REQUEST);
        }
        if (Objects.isNull(createPurchaseRequest.getSkuId())) {
            throw new BadRequestException(ErrorMessage.SKU_ID_REQUIRED);
        }
        if (Objects.isNull(createPurchaseRequest.getQuantity())) {
            throw new BadRequestException(ErrorMessage.QUANTITY_REQUIRED);
        }
        if (EntityValidatorUtil.isInvalidQuantity(createPurchaseRequest.getQuantity())) {
            throw new BadRequestException(ErrorMessage.INVALID_QUANTITY);
        }
    }
}
