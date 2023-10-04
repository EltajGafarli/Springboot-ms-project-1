package com.example.productservice.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProductResponse {

    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
}
