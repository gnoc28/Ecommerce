package com.example.ecommerce.DAO.impl;

import com.example.ecommerce.DAO.InventoryDAO;
import com.example.ecommerce.entity.Inventories;
import com.example.ecommerce.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InventoryDAOImpl implements InventoryDAO {

    private final InventoryRepository repository;

    @Override
    public Inventories save(Inventories inventory){return repository.save(inventory);}

    @Override
    public Optional<Inventories> findByInventoryCode(String inventoryCode){
        return repository.findByInventoryCode(inventoryCode);
    }

    @Override
    public Optional<Inventories> findByProduct_ProductCode(String productCode){
        return repository.findByProduct_ProductCode(productCode);
    };

    @Override
    public Page<Inventories> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }
}
