package com.example.ecommerce.mapper;

import com.example.ecommerce.DTO.response.CategoryAdminResponse;
import com.example.ecommerce.DTO.response.CategoryCustomerResponse;
import com.example.ecommerce.entity.Categories;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    CategoryAdminResponse toCategoryAdminResponse(Categories category);

    CategoryCustomerResponse toCategoryCustomerResponse(Categories category);
}
