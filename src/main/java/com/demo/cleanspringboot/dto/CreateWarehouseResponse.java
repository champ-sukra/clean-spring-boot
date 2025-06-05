package com.demo.cleanspringboot.dto;

public class CreateWarehouseResponse {
    
    private Long warehouseId;
    
    // Default constructor
    public CreateWarehouseResponse() {}
    
    // Constructor
    public CreateWarehouseResponse(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
    
    // Getters and Setters
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
}