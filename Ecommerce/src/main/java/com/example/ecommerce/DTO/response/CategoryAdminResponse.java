package com.example.ecommerce.DTO.response;

import com.example.ecommerce.enums.CategoryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAdminResponse {
    private Long id;
    private String categoryCode;
    private String name;
    private CategoryStatus status; // ACTIVE / INACTIVE
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
