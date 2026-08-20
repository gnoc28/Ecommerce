package com.example.ecommerce.repository;

import com.example.ecommerce.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    @Modifying
    @Query("DELETE FROM CartItems ci" +
            " where ci.cart.customer.customerCode = :customerCode" +
            " AND ci.product.productCode in :productCodes")
    void deleteItemsFromCart(@Param("customerCode") String customerCode,
                             @Param("productCodes")List<String> productCodes);
}
