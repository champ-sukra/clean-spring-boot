package com.demo.cleanspringboot.service;

import com.demo.cleanspringboot.dto.CreateProductRequest;
import com.demo.cleanspringboot.dto.UpdateProductRequest;
import com.demo.cleanspringboot.entity.ProductEntity;
import com.demo.cleanspringboot.exception.ProductNotFoundException;
import com.demo.cleanspringboot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
    
    public ProductEntity getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }
    
    public ProductEntity createProduct(CreateProductRequest request) {
        ProductEntity product = new ProductEntity();
        product.setId(generateProductId());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(request.getCategory());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setRating(request.getRating());
        product.setDescription(request.getDescription());
        
        return productRepository.save(product);
    }
    
    public ProductEntity updateProduct(String id, UpdateProductRequest request) {
        ProductEntity product = getProductById(id);
        
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getRating() != null) {
            product.setRating(request.getRating());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        
        return productRepository.save(product);
    }
    
    public void deleteProduct(String id) {
        ProductEntity product = getProductById(id);
        productRepository.delete(product);
    }
    
    private String generateProductId() {
        return "prod-" + UUID.randomUUID().toString().substring(0, 8);
    }
}