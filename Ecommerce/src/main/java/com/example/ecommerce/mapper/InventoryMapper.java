package com.example.ecommerce.mapper;

import com.example.ecommerce.DTO.response.InventoryResponse;
import com.example.ecommerce.entity.Inventories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    @Mapping(target = "productCode", source = "product.productCode")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "availableStock", expression = "java(inventories.getStock() - inventories.getReservedStock())")
    public InventoryResponse toInventoryResponse(Inventories inventories);
}
