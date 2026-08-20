package com.example.ecommerce.DAO;

import com.example.ecommerce.entity.Carts;

import java.util.Optional;

public interface CartDAO {
    Optional<Carts> findByUsername(String username);
    Carts save(Carts cart);
}
