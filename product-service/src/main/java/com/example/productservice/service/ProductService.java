package com.example.productservice.service;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public void createProduct(ProductRequest productRequest) {
        Product product = Product
                .builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        this.productRepository.save(product);

    }

    public List<ProductResponse> getProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(product -> ProductResponse
                        .builder()
                        .productId(product.getId())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .name(product.getName())
                        .build()
                ).toList();
    }
}
