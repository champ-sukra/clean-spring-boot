
package com.demo.cleanspringboot.dto;

import java.util.List;

public class WarehouseResponse {

    private Long warehouseId;
    private String name;
    private Long sellerId;
    private String status;
    private String address;
    private List<SkuItem> skus;

    // Default constructor
    public WarehouseResponse() {}

    // Constructor
    public WarehouseResponse(Long warehouseId, String name, Long sellerId, String status, String address) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.sellerId = sellerId;
        this.status = status;
        this.address = address;
    }

    // Getters and Setters
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<SkuItem> getSkus() { return skus; }
    public void setSkus(List<SkuItem> skus) { this.skus = skus; }
}