package com.demo.cleanspringboot.infrastructure.adapter.out.persistence;

import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for PromotionAction
 */
@Repository
public interface PromotionActionRepository extends JpaRepository<PromotionActionEntity, Long> {

    List<PromotionActionEntity> findByRuleId(Long ruleId);
}

