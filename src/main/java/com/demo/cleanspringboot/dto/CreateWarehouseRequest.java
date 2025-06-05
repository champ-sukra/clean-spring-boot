package com.demo.cleanspringboot.dto;

import com.demo.cleanspringboot.model.WarehouseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateWarehouseRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Seller ID is required")
    private Long sellerId;
    
    @NotNull(message = "Status is required")
    private WarehouseStatus status;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    // Default constructor
    public CreateWarehouseRequest() {}
    
    // Constructor
    public CreateWarehouseRequest(String name, Long sellerId, WarehouseStatus status, String address) {
        this.name = name;
        this.sellerId = sellerId;
        this.status = status;
        this.address = address;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    
    public WarehouseStatus getStatus() { return status; }
    public void setStatus(WarehouseStatus status) { this.status = status; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}