package com.demo.cleanspringboot.repository;

import com.demo.cleanspringboot.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("SELECT w FROM Warehouse w LEFT JOIN FETCH w.skus WHERE w.warehouseId = :id")
    Optional<Warehouse> findByIdWithSkus(@Param("id") Long id);
}