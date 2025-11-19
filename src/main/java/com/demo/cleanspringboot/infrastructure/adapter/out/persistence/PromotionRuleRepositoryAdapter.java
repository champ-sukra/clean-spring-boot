package com.demo.cleanspringboot.infrastructure.adapter.out.persistence;

import com.demo.cleanspringboot.application.port.out.PromotionRuleRepositoryPort;
import com.demo.cleanspringboot.domain.model.PromotionRule;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionRuleEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.mapper.PromotionRuleEntityMapper;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.repository.JpaPromotionRuleRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter implementing the PromotionRuleRepositoryPort
 * Bridges between domain (port) and infrastructure (JPA)
 */
@Component
public class PromotionRuleRepositoryAdapter implements PromotionRuleRepositoryPort {

    private final JpaPromotionRuleRepository jpaPromotionRuleRepository;
    private final PromotionRuleEntityMapper promotionRuleEntityMapper;

    public PromotionRuleRepositoryAdapter(
            JpaPromotionRuleRepository jpaPromotionRuleRepository,
            PromotionRuleEntityMapper promotionRuleEntityMapper) {
        this.jpaPromotionRuleRepository = jpaPromotionRuleRepository;
        this.promotionRuleEntityMapper = promotionRuleEntityMapper;
    }

    @Override
    public Optional<PromotionRule> findById(Long id) {
        return jpaPromotionRuleRepository.findById(id)
            .map(promotionRuleEntityMapper::toDomain);
    }

    @Override
    public PromotionRule save(PromotionRule promotionRule) {
        PromotionRuleEntity entity = promotionRuleEntityMapper.toEntity(promotionRule);
        PromotionRuleEntity savedEntity = jpaPromotionRuleRepository.save(entity);
        return promotionRuleEntityMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        jpaPromotionRuleRepository.deleteById(id);
    }
}

