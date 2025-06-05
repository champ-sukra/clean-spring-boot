package com.demo.cleanspringboot.service;

import com.demo.cleanspringboot.dto.*;

import java.util.List;

public interface WarehouseService {
    CreateWarehouseResponse createWarehouse(CreateWarehouseRequest request);
    WarehouseResponse getWarehouseById(Long id);
    void assignSkusToWarehouse(Long warehouseId, List<SkuAssignment> skuAssignments);
    void updateSkuInWarehouse(Long warehouseId, String sku, SkuUpdateRequest request);
}