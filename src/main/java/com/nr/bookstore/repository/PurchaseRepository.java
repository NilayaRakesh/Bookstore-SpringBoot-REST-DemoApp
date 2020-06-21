package com.nr.bookstore.repository;

import com.nr.bookstore.model.rds.Purchase;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
}
