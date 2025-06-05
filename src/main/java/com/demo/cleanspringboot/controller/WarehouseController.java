package com.demo.cleanspringboot.controller;

import com.demo.cleanspringboot.dto.*;
import com.demo.cleanspringboot.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    public ResponseEntity<?> createWarehouse(@Valid @RequestBody CreateWarehouseRequest request) {
        try {
            CreateWarehouseResponse response = warehouseService.createWarehouse(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("WAREHOUSE_CREATION_FAILED", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWarehouseById(@PathVariable Long id) {
        try {
            WarehouseResponse response = warehouseService.getWarehouseById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse("WAREHOUSE_NOT_FOUND", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/skus")
    public ResponseEntity<?> assignSkusToWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody List<SkuAssignment> skuAssignments) {
        try {
            warehouseService.assignSkusToWarehouse(id, skuAssignments);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                ErrorResponse error = new ErrorResponse("WAREHOUSE_NOT_FOUND", e.getMessage());
                return ResponseEntity.notFound().build();
            }
            ErrorResponse error = new ErrorResponse("SKU_ASSIGNMENT_FAILED", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}/skus/{sku}")
    public ResponseEntity<?> updateSkuInWarehouse(
            @PathVariable Long id,
            @PathVariable String sku,
            @Valid @RequestBody SkuUpdateRequest request) {
        try {
            warehouseService.updateSkuInWarehouse(id, sku, request);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                ErrorResponse error = new ErrorResponse("WAREHOUSE_OR_SKU_NOT_FOUND", e.getMessage());
                return ResponseEntity.notFound().build();
            }
            ErrorResponse error = new ErrorResponse("SKU_UPDATE_FAILED", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}