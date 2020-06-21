package com.nr.bookstore.service.impl;

import com.nr.bookstore.constant.ErrorMessage;
import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.log.Logger;
import com.nr.bookstore.manager.PurchaseTransactionsManager;
import com.nr.bookstore.model.api.CreatePurchaseRequest;
import com.nr.bookstore.model.api.CreatePurchaseResponse;
import com.nr.bookstore.model.dto.PurchaseDto;
import com.nr.bookstore.model.rds.Purchase;
import com.nr.bookstore.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private static final Logger LOGGER = new Logger(PurchaseServiceImpl.class);

    private PurchaseTransactionsManager purchaseTransactionsManager;

    @Autowired
    public PurchaseServiceImpl(PurchaseTransactionsManager purchaseTransactionsManager) {
        this.purchaseTransactionsManager = purchaseTransactionsManager;
    }

    @Override
    public CreatePurchaseResponse createPurchase(CreatePurchaseRequest createPurchaseRequest)
            throws NotFoundException, BadRequestException, InternalException {

        PurchaseDto.PurchaseDtoBuilder purchaseDtoBuilder = PurchaseDto.builder()
                .skuId(createPurchaseRequest.getSkuId())
                .quantityBought(createPurchaseRequest.getQuantity());

        Purchase purchase;
        try {
            purchase = purchaseTransactionsManager.createPurchase(purchaseDtoBuilder.build());

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Constraint violated while creating purchase", e);
            throw new BadRequestException(ErrorMessage.CONSTRAINT_VIOLATED);

        } catch (DataAccessException e) {
            LOGGER.error("DB Exception while creating purchase", e);
            throw new InternalException(ErrorMessage.DB_ERROR);
        }

        purchaseDtoBuilder.purchaseId(purchase.getPurchaseId())
                .price(purchase.getSku().getPrice());
        return new CreatePurchaseResponse(purchaseDtoBuilder.build());

    }
}
