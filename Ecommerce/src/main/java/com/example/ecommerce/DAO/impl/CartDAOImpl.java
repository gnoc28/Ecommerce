package com.example.ecommerce.DAO.impl;

import com.example.ecommerce.DAO.CartDAO;
import com.example.ecommerce.entity.Carts;
import com.example.ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartDAOImpl implements CartDAO {
    private final CartRepository repository;

    @Override
    public Optional<Carts> findByUsername(String username){
        return repository.findByCustomer_Username(username);
    }

    @Override
    public Carts save(Carts cart){
        return repository.save(cart);
    }
}
