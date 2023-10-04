package com.example.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "products")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String description;

    @Column(length = 50)
    private BigDecimal price;

    @Override
    public int hashCode() {
        return Objects
                .hash(
                  id,
                  name,
                  description,
                  price
                );
    }

    @Override
    public boolean equals(Object o) {
        Product product = (Product) o;

        return product.id == id
                && Objects.equals(name, product.name)
                && Objects.equals(description, product.description)
                && Objects.equals(price, product.price);
    }
}
