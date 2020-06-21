package com.nr.bookstore.manager;

import com.nr.bookstore.constant.ErrorMessage;
import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.dto.PurchaseDto;
import com.nr.bookstore.model.dto.SkuDto;
import com.nr.bookstore.model.rds.Purchase;
import com.nr.bookstore.model.rds.Sku;
import com.nr.bookstore.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PurchaseTransactionsManager {

    private PurchaseRepository purchaseRepository;
    private SkuTransactionsManager skuTransactionsManager;

    @Autowired
    public PurchaseTransactionsManager(PurchaseRepository purchaseRepository, SkuTransactionsManager skuTransactionsManager) {
        this.purchaseRepository = purchaseRepository;
        this.skuTransactionsManager = skuTransactionsManager;
    }

    @Transactional(rollbackOn = Exception.class)
    public Purchase createPurchase(PurchaseDto purchaseDto)
            throws NotFoundException, BadRequestException {

        Sku sku = updateSku(purchaseDto);
        Purchase purchase = createPurchase(purchaseDto, sku);
        return purchase;
    }


    private Purchase createPurchase(PurchaseDto purchaseDto, Sku sku) {
        Purchase purchase = new Purchase(sku, sku.getPrice(), purchaseDto.getQuantityBought());
        return purchaseRepository.save(purchase);
    }


    private Sku updateSku(PurchaseDto purchaseDto)
            throws NotFoundException, BadRequestException {

        Sku sku = skuTransactionsManager.getSku(purchaseDto.getSkuId());
        if (sku.getQuantity() < purchaseDto.getQuantityBought()) {
            throw new BadRequestException(ErrorMessage.INSUFFICIENT_QUANTITY);
        }

        SkuDto skuDto = SkuDto.builder()
                .quantity(sku.getQuantity() - purchaseDto.getQuantityBought())
                .build();
        return skuTransactionsManager.updateSku(purchaseDto.getSkuId(), skuDto);
    }
}
