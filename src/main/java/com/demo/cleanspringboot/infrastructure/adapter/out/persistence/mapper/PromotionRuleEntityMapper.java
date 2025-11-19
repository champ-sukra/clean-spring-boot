package com.demo.cleanspringboot.infrastructure.adapter.out.persistence.mapper;

import com.demo.cleanspringboot.domain.model.PromotionRule;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionRuleEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between JPA Entity and Domain model
 */
@Component
public class PromotionRuleEntityMapper {

    public PromotionRule toDomain(PromotionRuleEntity entity) {
        if (entity == null) {
            return null;
        }

        PromotionRule domain = PromotionRule.create(
            entity.getTemplateId(),
            entity.getRuleName(),
            entity.getStartDate(),
            entity.getEndDate()
        );

        // Use package-private setters to reconstruct full state
        domain.setId(entity.getId());
        domain.setActive(entity.getActive());
        domain.setPriority(entity.getPriority());
        domain.setQuota(entity.getQuota());
        domain.setQuotaUsed(entity.getQuotaUsed());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());

        return domain;
    }

    public PromotionRuleEntity toEntity(PromotionRule domain) {
        if (domain == null) {
            return null;
        }

        PromotionRuleEntity entity = new PromotionRuleEntity();
        entity.setId(domain.getId());
        entity.setTemplateId(domain.getTemplateId());
        entity.setRuleName(domain.getRuleName());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setActive(domain.getActive());
        entity.setPriority(domain.getPriority());
        entity.setQuota(domain.getQuota());
        entity.setQuotaUsed(domain.getQuotaUsed());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }
}

