package com.nr.bookstore.manager;

import com.nr.bookstore.constant.ErrorMessage;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.dto.SkuDto;
import com.nr.bookstore.model.rds.CatalogItem;
import com.nr.bookstore.model.rds.Sku;
import com.nr.bookstore.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkuTransactionsManager {

    private SkuRepository skuRepository;

    @Autowired
    public SkuTransactionsManager(SkuRepository skuRepository) {
        this.skuRepository = skuRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public List<Sku> saveSkus(List<SkuDto> skuDtos, final CatalogItem catalogItem) {
        List<Sku> skusToSave = skuDtos.stream()
                .map(skuDto -> buildSku(skuDto, catalogItem))
                .collect(Collectors.toList());

        Iterable<Sku> savedSkus = skuRepository.saveAll(skusToSave);
        List<Sku> savedSkusList = new ArrayList<>();
        savedSkus.forEach(savedSkusList::add);
        return savedSkusList;
    }


    @Transactional(rollbackOn = Exception.class)
    public Sku updateSku(Long skuId, SkuDto skuDto) throws NotFoundException {
        Sku sku = getSku(skuId);

        if (Objects.nonNull(skuDto.getPrice())) {
            sku.setPrice(skuDto.getPrice());
        }

        if (Objects.nonNull(skuDto.getQuantity())) {
            sku.setQuantity(skuDto.getQuantity());
        }
        return skuRepository.save(sku);
    }


    public Sku getSku(Long skuId) throws NotFoundException {
        Optional<Sku> sku = skuRepository.findById(skuId);
        return sku.orElseThrow(() -> new NotFoundException(ErrorMessage.SKU_NOT_FOUND));
    }


    public List<Sku> getSkusByCatalogItemId(Long catalogItemId) {
        return skuRepository.findByCatalogItemCatalogItemId(catalogItemId);
    }


    private Sku buildSku(SkuDto skuDto, CatalogItem catalogItem) {
        Sku sku = new Sku();
        sku.setCatalogItem(catalogItem);
        sku.setPrice(skuDto.getPrice());
        sku.setQuantity(skuDto.getQuantity());
        return sku;
    }
}
