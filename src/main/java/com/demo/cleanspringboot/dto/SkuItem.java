
package com.demo.cleanspringboot.dto;

public class SkuItem {
    
    private String sku;
    private Double price;
    private Short qty;
    
    // Default constructor
    public SkuItem() {}
    
    // Constructor
    public SkuItem(String sku, Double price, Short qty) {
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