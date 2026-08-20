package com.example.ecommerce.mapper;

import com.example.ecommerce.DTO.request.UpdateProductRequest;
import com.example.ecommerce.DTO.response.ProductAdminDetailResponse;
import com.example.ecommerce.DTO.response.ProductAdminResponse;
import com.example.ecommerce.DTO.response.ProductCustomerDetailResponse;
import com.example.ecommerce.DTO.response.ProductCustomerResponse;
import com.example.ecommerce.entity.Products;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "quantity", source = "inventory.stock")
    @Mapping(target = "outOfStock", expression = "java(product.getInventory() == null || product.getInventory().getStock() <= 0)")
    ProductCustomerResponse toProductCustomerResponse(Products product);

    @Mapping(target = "quantity", source = "inventory.stock")
    @Mapping(target = "outOfStock", expression = "java(product.getInventory() == null || product.getInventory().getStock() <= 0)")
    ProductCustomerDetailResponse toProductCustomerDetailResponse(Products product);

    @Mapping(target = "quantity", source = "inventory.stock")
    @Mapping(target = "reservedStock", source = "inventory.reservedStock")
    @Mapping(target = "outOfStock", expression = "java(product.getInventory() == null || product.getInventory().getStock() <= 0)")
    ProductAdminResponse toProductAdminResponse(Products product);

    @Mapping(target = "quantity", source = "inventory.stock")
    @Mapping(target = "reservedStock", source = "inventory.reservedStock")
    @Mapping(target = "outOfStock", expression = "java(product.getInventory() == null || product.getInventory().getStock() <= 0)")
    ProductAdminDetailResponse toProductAdminDetailResponse(Products product);

    void updateProductFromDto(UpdateProductRequest dto,@MappingTarget Products product);
}
