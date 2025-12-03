package com.demo.cleanspringboot.infrastructure.adapter.out.persistence;

import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionRuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for PromotionRule
 * Direct persistence access as shown in sequence diagram
 */
@Repository
public interface PromotionRuleRepository extends JpaRepository<PromotionRuleEntity, Long> {

    @Query("SELECT p FROM PromotionRuleEntity p WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:templateId IS NULL OR p.templateId = :templateId)")
    Page<PromotionRuleEntity> findByFilters(
        @Param("status") Integer status,
        @Param("templateId") Long templateId,
        Pageable pageable);

    /**
     * Find all active promotion rules for building rule index
     * Status 2 = ACTIVE
     */
    @Query("SELECT p FROM PromotionRuleEntity p WHERE p.status = 2")
    List<PromotionRuleEntity> findActivePromotionRules();
}

