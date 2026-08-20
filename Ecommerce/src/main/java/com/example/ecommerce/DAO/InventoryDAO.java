package com.example.ecommerce.DAO;

import com.example.ecommerce.entity.Inventories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InventoryDAO {
    Inventories save(Inventories inventories);
    Optional<Inventories> findByInventoryCode(String inventoryCode);
    Optional<Inventories> findByProduct_ProductCode(String productCode);
    Page<Inventories> findAll(Pageable pageable);
}

