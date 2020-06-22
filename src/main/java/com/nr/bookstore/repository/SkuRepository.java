package com.nr.bookstore.repository;

import com.nr.bookstore.model.rds.Sku;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SkuRepository extends CrudRepository<Sku, Long> {

    List<Sku> findByCatalogItemCatalogItemId(Long catalogItemId);
}
