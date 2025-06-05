package com.demo.cleanspringboot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "warehouse_skus")
public class WarehouseSku {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
    
    @Column(name = "sku", nullable = false)
    private String sku;
    
    @Column(name = "price", nullable = false)
    private Double price;
    
    @Column(name = "qty", nullable = false)
    private Short qty;
    
    // Default constructor
    public WarehouseSku() {}
    
    // Constructor
    public WarehouseSku(Warehouse warehouse, String sku, Double price, Short qty) {
        this.warehouse = warehouse;
        this.sku = sku;
        this.price = price;
        this.qty = qty;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Short getQty() { return qty; }
    public void setQty(Short qty) { this.qty = qty; }
}