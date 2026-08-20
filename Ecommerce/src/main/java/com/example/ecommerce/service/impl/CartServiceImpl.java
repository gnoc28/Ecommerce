package com.example.ecommerce.service.impl;

import com.example.ecommerce.DAO.CartDAO;
import com.example.ecommerce.DAO.ProductDAO;
import com.example.ecommerce.DTO.request.CartItemRequest;
import com.example.ecommerce.DTO.request.DeleteCartRequest;
import com.example.ecommerce.DTO.request.UpdateCartRequest;
import com.example.ecommerce.DTO.response.CartItemResponse;
import com.example.ecommerce.DTO.response.CartResponse;
import com.example.ecommerce.entity.CartItems;
import com.example.ecommerce.entity.Carts;
import com.example.ecommerce.entity.Products;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.exception.AppException;
import com.example.ecommerce.mapper.CartMapper;
import com.example.ecommerce.security.CustomerUserDetails;
import com.example.ecommerce.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartDAO dao;
    private final ProductDAO productDAO;
    private final CartMapper mapper;

    @Override
    public CartResponse getCart(){
        Carts cart = dao.findByUsername(CustomerUserDetails.getCurrentUsername())
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy giỏ hàng"));
        CartResponse response = mapper.toCartResponse(cart);
        List<CartItems> itemsList = cart.getCartItems();
        List<CartItemResponse> responseList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(var item : itemsList){
            BigDecimal price = item.getProduct().getPrice();
            Integer quantity = item.getQuantity();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(subtotal);

            CartItemResponse itemResponse = CartItemResponse.builder()
                    .id(item.getId())
                    .name(item.getProduct().getName())
                    .imageUrl(item.getProduct().getImageUrl())
                    .price(price)
                    .quantity(quantity)
                    .subtotal(subtotal)
                    .build();

            responseList.add(itemResponse);
        }
        return CartResponse.builder()
                .items(responseList)
                .totalAmount(totalAmount)
                .build();
    };

    @Override
    public CartResponse deleteCart(DeleteCartRequest request){
        Carts cart = dao.findByUsername(CustomerUserDetails.getCurrentUsername())
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy giỏ hàng"));

        List<CartItems> itemsList = cart.getCartItems();

        if(itemsList != null && !itemsList.isEmpty()){
            List<Long> items = request != null ? request.getCartItemIds() : null;

            if(items == null || items.isEmpty()){
                itemsList.clear();
            } else {
                itemsList.removeIf(item -> items.contains(item.getId()));
            }

            dao.save(cart);
        }
        return getCart();
    };

    @Override
    @Transactional
    public CartResponse addToCart(CartItemRequest request){
        Carts cart = dao.findByUsername(CustomerUserDetails.getCurrentUsername())
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy giỏ hàng"));

        Products product = productDAO.findByProductCode(request.getProductCode())
                .orElseThrow(() -> new AppException.NotFoundException("Sản phẩm không tồn tại"));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new AppException.BadRequestException("Sản phẩm hiện không khả dụng");
        }

        int stock = (product.getInventory() != null) ? product.getInventory().getStock() : 0;

        List<CartItems> itemsList = cart.getCartItems();
        if (itemsList == null) {
            itemsList = new ArrayList<>();
            cart.setCartItems(itemsList);
        }

        Optional<CartItems> existingItem = itemsList.stream()
                .filter(item -> item.getProduct().getProductCode().equals(request.getProductCode()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItems item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();

            if (newQuantity > stock) {
                throw new AppException.BadRequestException("Vượt quá số lượng cho phép");
            }

            item.setQuantity(newQuantity);
        } else {

            if (request.getQuantity() > stock) {
                throw new AppException.BadRequestException("Vượt quá số lượng cho phép");
            }

            CartItems newItem = CartItems.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            itemsList.add(newItem);
        }

        dao.save(cart);
        return getCart();
    }

    @Override
    @Transactional
    public CartResponse updateCart(Long cartItemId, UpdateCartRequest request){
        Carts cart = dao.findByUsername(CustomerUserDetails.getCurrentUsername())
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy giỏ hàng"));

        CartItems itemUpdate = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy sản phẩm"));

        Products product = itemUpdate.getProduct();

        int stock = (product.getInventory() != null) ? product.getInventory().getStock() : 0;

        if (request.getQuantity() > stock) {
            throw new AppException.BadRequestException("Vượt quá số lượng cho phép");
        }

        itemUpdate.setQuantity(request.getQuantity());
        dao.save(cart);

        return getCart();
    }
}
