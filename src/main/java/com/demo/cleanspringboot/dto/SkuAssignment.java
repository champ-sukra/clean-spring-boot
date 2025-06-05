package com.demo.cleanspringboot.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SkuAssignment {
    
    @NotBlank(message = "SKU is required")
    private String sku;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be non-negative")
    private Double price;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    private Short qty;
    
    // Default constructor
    public SkuAssignment() {}
    
    // Constructor
    public SkuAssignment(String sku, Double price, Short qty) {
        this.sku = sku;
        this.price = price;
        this.qty = qty;
    }
    
    // Getters and Setters
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Short getQty() { return qty; }
    public void setQty(Short qty) { this.qty = qty; }
}