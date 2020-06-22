package com.nr.bookstore.service;

import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.api.CreatePurchaseRequest;
import com.nr.bookstore.model.api.CreatePurchaseResponse;


public interface PurchaseService {

    CreatePurchaseResponse createPurchase(CreatePurchaseRequest createPurchaseRequest) throws NotFoundException, BadRequestException, InternalException;
}
