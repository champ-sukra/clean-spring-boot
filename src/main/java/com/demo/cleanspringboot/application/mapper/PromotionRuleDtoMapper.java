package com.demo.cleanspringboot.application.mapper;

import com.demo.cleanspringboot.application.dto.response.PromotionRuleResponse;
import com.demo.cleanspringboot.domain.model.PromotionRule;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Domain model and DTO
 */
@Component
public class PromotionRuleDtoMapper {

    public PromotionRuleResponse toResponse(PromotionRule promotionRule) {
        if (promotionRule == null) {
            return null;
        }

        return new PromotionRuleResponse()
            .id(promotionRule.getId())
            .templateId(promotionRule.getTemplateId())
            .ruleName(promotionRule.getRuleName())
            .startDate(promotionRule.getStartDate())
            .endDate(promotionRule.getEndDate())
            .active(promotionRule.getActive())
            .priority(promotionRule.getPriority())
            .quota(promotionRule.getQuota())
            .quotaUsed(promotionRule.getQuotaUsed())
            .createdAt(promotionRule.getCreatedAt())
            .updatedAt(promotionRule.getUpdatedAt());
    }
}

