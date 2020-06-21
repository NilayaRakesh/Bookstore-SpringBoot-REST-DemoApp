package com.nr.bookstore.service.impl;

import com.nr.bookstore.constant.ErrorMessage;
import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.log.Logger;
import com.nr.bookstore.manager.SkuTransactionsManager;
import com.nr.bookstore.model.dto.SkuDto;
import com.nr.bookstore.model.rds.CatalogItem;
import com.nr.bookstore.model.rds.Sku;
import com.nr.bookstore.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    private SkuTransactionsManager skuTransactionsManager;

    private static final Logger LOGGER = new Logger(SkuServiceImpl.class);

    @Autowired
    public SkuServiceImpl(SkuTransactionsManager skuTransactionsManager) {
        this.skuTransactionsManager = skuTransactionsManager;
    }

    @Override
    public List<Sku> saveSkus(List<SkuDto> skuDtos, final CatalogItem catalogItem)
            throws InternalException, BadRequestException {

        try {
            return skuTransactionsManager.saveSkus(skuDtos, catalogItem);

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Constraint violated while saving skus", e);
            throw new BadRequestException(ErrorMessage.CONSTRAINT_VIOLATED);

        } catch (DataAccessException e) {
            LOGGER.error("Exception while saving skus", e);
            throw new InternalException(ErrorMessage.DB_ERROR);
        }
    }


    @Override
    public Sku updateSku(Long skuId, SkuDto skuDto)
            throws NotFoundException, InternalException, BadRequestException {

        try {
            return skuTransactionsManager.updateSku(skuId, skuDto);

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Constraint violated while updating skus", e);
            throw new BadRequestException(ErrorMessage.CONSTRAINT_VIOLATED);

        } catch (DataAccessException e) {
            LOGGER.error("Exception while updating skus", e);
            throw new InternalException(ErrorMessage.DB_ERROR);
        }
    }


    @Override
    public Sku getSku(Long skuId) throws NotFoundException, InternalException {
        try {
            return skuTransactionsManager.getSku(skuId);

        } catch (DataAccessException e) {
            LOGGER.error("Exception while fetching skus", e);
            throw new InternalException(ErrorMessage.DB_ERROR);
        }
    }


    @Override
    public List<Sku> getSkusByCatalogItemId(Long catalogItemId) throws InternalException {
        try {
            return skuTransactionsManager.getSkusByCatalogItemId(catalogItemId);

        } catch (DataAccessException e) {
            LOGGER.error("Exception while fetching skus", e);
            throw new InternalException(ErrorMessage.DB_ERROR);
        }
    }
}
