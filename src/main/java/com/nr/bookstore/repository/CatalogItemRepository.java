package com.nr.bookstore.repository;

import com.nr.bookstore.model.rds.CatalogItem;
import org.springframework.data.repository.CrudRepository;

public interface CatalogItemRepository<T extends CatalogItem> extends CrudRepository<T, Long> {
}
