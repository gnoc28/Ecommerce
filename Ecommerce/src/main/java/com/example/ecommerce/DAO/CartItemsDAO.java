package com.example.ecommerce.DAO;

import java.util.List;

public interface CartItemsDAO {

    void deleteItemsFromCart(String customerCode, List<String> productCodes);
}
