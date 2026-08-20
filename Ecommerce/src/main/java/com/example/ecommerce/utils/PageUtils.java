package com.example.ecommerce.utils;


import com.example.ecommerce.DTO.response.PageResponse;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
public class PageUtils {
    public static <T> PageResponse<T> toPageResponse(Page<T> page){
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}

