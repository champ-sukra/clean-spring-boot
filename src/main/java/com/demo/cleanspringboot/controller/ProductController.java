package com.demo.cleanspringboot.controller;

import com.demo.cleanspringboot.dto.CreateProductRequest;
import com.demo.cleanspringboot.dto.ErrorResponse;
import com.demo.cleanspringboot.dto.UpdateProductRequest;
import com.demo.cleanspringboot.entity.ProductEntity;
import com.demo.cleanspringboot.exception.ProductNotFoundException;
import com.demo.cleanspringboot.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> products = productService.getAllProducts();

        System.out.println("products: " + products);

        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        try {
            ProductEntity product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            ErrorResponse error = new ErrorResponse("product_not_found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductRequest request, 
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Invalid input");
            ErrorResponse error = new ErrorResponse("invalid_input", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        ProductEntity product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, 
                                         @Valid @RequestBody UpdateProductRequest request,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Invalid input");
            ErrorResponse error = new ErrorResponse("invalid_input", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        
        try {
            ProductEntity product = productService.updateProduct(id, request);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            ErrorResponse error = new ErrorResponse("product_not_found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            ErrorResponse error = new ErrorResponse("product_not_found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}