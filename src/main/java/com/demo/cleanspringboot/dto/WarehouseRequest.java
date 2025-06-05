package com.demo.cleanspringboot.dto;

import com.demo.cleanspringboot.model.WarehouseStatus;
import jakarta.validation.constraints.*;

public class WarehouseRequest {
    
    @NotBlank(message = "Warehouse code is required")
    @Size(min = 2, max = 10, message = "Warehouse code must be between 2 and 10 characters")
    private String code;
    
    @NotBlank(message = "Warehouse name is required")
    @Size(min = 1, max = 100, message = "Warehouse name must be between 1 and 100 characters")
    private String name;
    
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
    
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;
    
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;
    
    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;
    
    @Size(max = 100, message = "Manager name must not exceed 100 characters")
    private String managerName;
    
    @Email(message = "Contact email must be valid")
    @Size(max = 100, message = "Contact email must not exceed 100 characters")
    private String contactEmail;
    
    @Size(max = 20, message = "Contact phone must not exceed 20 characters")
    private String contactPhone;
    
    @Min(value = 0, message = "Storage capacity must be non-negative")
    @Max(value = 1000000, message = "Storage capacity must not exceed 1,000,000")
    private Integer storageCapacity;
    
    @Min(value = 0, message = "Current utilization must be non-negative")
    private Integer currentUtilization;
    
    private WarehouseStatus status;
    
    // Constructors
    public WarehouseRequest() {}
    
    public WarehouseRequest(String code, String name, String address, String city, String country) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
    }
    
    // Getters and setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
    
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    
    public Integer getStorageCapacity() { return storageCapacity; }
    public void setStorageCapacity(Integer storageCapacity) { this.storageCapacity = storageCapacity; }
    
    public Integer getCurrentUtilization() { return currentUtilization; }
    public void setCurrentUtilization(Integer currentUtilization) { this.currentUtilization = currentUtilization; }
    
    public WarehouseStatus getStatus() { return status; }
    public void setStatus(WarehouseStatus status) { this.status = status; }
}