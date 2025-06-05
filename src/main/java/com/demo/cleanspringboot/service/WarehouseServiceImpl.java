package com.demo.cleanspringboot.service;

import com.demo.cleanspringboot.dto.*;
import com.demo.cleanspringboot.model.Warehouse;
import com.demo.cleanspringboot.model.WarehouseSku;
import com.demo.cleanspringboot.model.WarehouseStatus;
import com.demo.cleanspringboot.repository.WarehouseRepository;
import com.demo.cleanspringboot.repository.WarehouseSkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseSkuRepository warehouseSkuRepository;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                WarehouseSkuRepository warehouseSkuRepository) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseSkuRepository = warehouseSkuRepository;
    }

    @Override
    public CreateWarehouseResponse createWarehouse(CreateWarehouseRequest request) {
        WarehouseStatus status = WarehouseStatus.valueOf(request.getStatus().toString().toUpperCase());

        Warehouse warehouse = new Warehouse(
                request.getName(),
                request.getSellerId(),
                status,
                request.getAddress()
        );

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return new CreateWarehouseResponse(savedWarehouse.getWarehouseId());
    }

    @Override
    public WarehouseResponse getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findByIdWithSkus(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        List<SkuItem> skuItems = warehouse.getSkus().stream()
                .map(sku -> new SkuItem(sku.getSku(), sku.getPrice(), sku.getQty()))
                .collect(Collectors.toList());

        WarehouseResponse response = new WarehouseResponse(
                warehouse.getWarehouseId(),
                warehouse.getName(),
                warehouse.getSellerId(),
                warehouse.getStatus().name().toLowerCase(),
                warehouse.getAddress()
        );
        response.setSkus(skuItems);

        return response;
    }

    @Override
    public void assignSkusToWarehouse(Long warehouseId, List<SkuAssignment> skuAssignments) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        for (SkuAssignment assignment : skuAssignments) {
            WarehouseSku warehouseSku = new WarehouseSku(
                    warehouse,
                    assignment.getSku(),
                    assignment.getPrice(),
                    assignment.getQty()
            );
            warehouseSkuRepository.save(warehouseSku);
        }
    }

    @Override
    public void updateSkuInWarehouse(Long warehouseId, String sku, SkuUpdateRequest request) {
        WarehouseSku warehouseSku = warehouseSkuRepository
                .findByWarehouse_WarehouseIdAndSku(warehouseId, sku)
                .orElseThrow(() -> new RuntimeException("Warehouse or SKU not found"));

        warehouseSku.setPrice(request.getPrice());
        warehouseSku.setQty(request.getQty());
        warehouseSkuRepository.save(warehouseSku);
    }
}