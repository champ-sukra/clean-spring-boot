package com.demo.cleanspringboot.infrastructure.adapter.out.persistence.repository;

import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for PromotionRuleEntity
 */
@Repository
public interface JpaPromotionRuleRepository extends JpaRepository<PromotionRuleEntity, Long> {

    // Spring Data JPA provides findById, save, delete, etc. automatically
}

