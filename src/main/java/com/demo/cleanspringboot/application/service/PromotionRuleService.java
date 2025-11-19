package com.demo.cleanspringboot.application.service;

import com.demo.cleanspringboot.application.dto.response.PromotionRuleResponse;
import com.demo.cleanspringboot.application.mapper.PromotionRuleDtoMapper;
import com.demo.cleanspringboot.application.port.out.PromotionRuleRepositoryPort;
import com.demo.cleanspringboot.domain.exception.ResourceNotFoundException;
import com.demo.cleanspringboot.domain.model.PromotionRule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for PromotionRule operations
 * Orchestrates between domain and ports
 */
@Service
@Transactional(readOnly = true)
public class PromotionRuleService {

    private final PromotionRuleRepositoryPort promotionRuleRepositoryPort;
    private final PromotionRuleDtoMapper promotionRuleDtoMapper;

    public PromotionRuleService(
            PromotionRuleRepositoryPort promotionRuleRepositoryPort,
            PromotionRuleDtoMapper promotionRuleDtoMapper) {
        this.promotionRuleRepositoryPort = promotionRuleRepositoryPort;
        this.promotionRuleDtoMapper = promotionRuleDtoMapper;
    }

    public PromotionRuleResponse getPromotionRuleDetail(Long ruleId) {
        PromotionRule promotionRule = promotionRuleRepositoryPort.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("PromotionRule", ruleId));

        return promotionRuleDtoMapper.toResponse(promotionRule);
    }
}

