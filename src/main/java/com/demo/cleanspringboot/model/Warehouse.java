package com.demo.cleanspringboot.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "warehouses")
public class Warehouse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Long warehouseId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WarehouseStatus status;
    
    @Column(name = "address", nullable = false)
    private String address;
    
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WarehouseSku> skus;
    
    // Default constructor
    public Warehouse() {}
    
    // Constructor
    public Warehouse(String name, Long sellerId, WarehouseStatus status, String address) {
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
    
    public WarehouseStatus getStatus() { return status; }
    public void setStatus(WarehouseStatus status) { this.status = status; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public List<WarehouseSku> getSkus() { return skus; }
    public void setSkus(List<WarehouseSku> skus) { this.skus = skus; }
}