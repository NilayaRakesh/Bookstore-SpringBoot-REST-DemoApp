package com.nr.bookstore.service;

import com.nr.bookstore.exception.BadRequestException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.dto.SkuDto;
import com.nr.bookstore.model.rds.CatalogItem;
import com.nr.bookstore.model.rds.Sku;

import java.util.List;

public interface SkuService {

    List<Sku> saveSkus(List<SkuDto> skuDtos, final CatalogItem catalogItem) throws InternalException, BadRequestException;

    Sku updateSku(Long skuId, SkuDto skuDto) throws NotFoundException, InternalException, BadRequestException;

    Sku getSku(Long skuId) throws NotFoundException, InternalException;

    List<Sku> getSkusByCatalogItemId(Long catalogItemId) throws InternalException;
}
