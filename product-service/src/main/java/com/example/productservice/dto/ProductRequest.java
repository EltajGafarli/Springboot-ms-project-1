package com.example.productservice.dto;

import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;

}
