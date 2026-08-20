package com.example.ecommerce.DAO.impl;

import com.example.ecommerce.DAO.CartItemsDAO;
import com.example.ecommerce.repository.CartItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartItemsDAOImpl implements CartItemsDAO {

    private final CartItemsRepository repository;

    @Override
    public void deleteItemsFromCart(String customerCode, List<String> productCodes){
        repository.deleteItemsFromCart(customerCode,productCodes);
    };
}
