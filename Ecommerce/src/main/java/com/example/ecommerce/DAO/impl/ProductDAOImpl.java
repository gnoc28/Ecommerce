package com.example.ecommerce.DAO.impl;

import com.example.ecommerce.DAO.ProductDAO;
import com.example.ecommerce.entity.Products;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductDAOImpl implements ProductDAO {

    private final ProductRepository repository;

    @Override
    public Optional<Products> findByProductCode(String productCode){
        return repository.findByProductCode(productCode);
    }

    @Override
    public Page<Products> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }


    @Override
    public Products save(Products product){
        return repository.save(product);
    }

    @Override
    public Long countByCategoryCode(String categoryCode){
        return repository.countByCategoryCode(categoryCode);
    }


    @Override
    public Page<Products> getProducts(String keyword,
                                     String categoryCode,
                                     ProductStatus status,
                                     Pageable pageable){
        return repository.getProducts(keyword, categoryCode, status, pageable);
    }

    @Override
    public void deleteByProductCode(String productCode){
        repository.deleteByProductCode(productCode);
    }


}
