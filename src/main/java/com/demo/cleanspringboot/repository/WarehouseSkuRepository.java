package com.demo.cleanspringboot.repository;

import com.demo.cleanspringboot.model.WarehouseSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WarehouseSkuRepository extends JpaRepository<WarehouseSku, Long> {

    Optional<WarehouseSku> findByWarehouse_WarehouseIdAndSku(Long warehouseId, String sku);
}