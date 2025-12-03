package com.demo.cleanspringboot.infrastructure.adapter.out.persistence;

import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionConditionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for PromotionCondition
 */
@Repository
public interface PromotionConditionRepository extends JpaRepository<PromotionConditionEntity, Long> {

    List<PromotionConditionEntity> findByRuleId(Long ruleId);
}

