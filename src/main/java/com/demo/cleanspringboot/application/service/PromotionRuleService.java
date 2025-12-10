package com.demo.cleanspringboot.application.service;
import com.demo.cleanspringboot.application.dto.response.PagedPromotionRuleResponse;
import com.demo.cleanspringboot.application.dto.response.PromotionRuleResponse;
import com.demo.cleanspringboot.application.dto.response.PromotionRuleSummaryResponse;
import com.demo.cleanspringboot.domain.exception.ResourceNotFoundException;
import com.demo.cleanspringboot.domain.model.EvaluatePromotionRule;
import com.demo.cleanspringboot.domain.model.RuleIndex;
import com.demo.cleanspringboot.domain.service.PromotionRuleIndexDomainService;
import com.demo.cleanspringboot.domain.service.PromotionRuleDomainService;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionActionEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionConditionEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionRuleEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.PromotionActionRepository;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.PromotionConditionRepository;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.PromotionRuleRepository;
import com.demo.cleanspringboot.infrastructure.cache.RuleCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Application Service for PromotionRule
 * As shown in sequence diagram: (application/service)
 */
@Service
@Transactional(readOnly = true)
public class PromotionRuleService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionRuleService.class);

    private final PromotionRuleRepository promotionRuleRepository;
    private final PromotionConditionRepository promotionConditionRepository;
    private final PromotionActionRepository promotionActionRepository;
    private final PromotionRuleIndexDomainService promotionRuleIndexDomainService;
    private final PromotionRuleDomainService promotionRuleDomainService;
    private final RuleCache ruleCache;

    public PromotionRuleService(
            PromotionRuleRepository promotionRuleRepository,
            PromotionConditionRepository promotionConditionRepository,
            PromotionActionRepository promotionActionRepository,
            PromotionRuleIndexDomainService promotionRuleIndexDomainService,
            PromotionRuleDomainService promotionRuleDomainService,
            RuleCache ruleCache) {
        this.promotionRuleRepository = promotionRuleRepository;
        this.promotionConditionRepository = promotionConditionRepository;
        this.promotionActionRepository = promotionActionRepository;
        this.promotionRuleIndexDomainService = promotionRuleIndexDomainService;
        this.promotionRuleDomainService = promotionRuleDomainService;
        this.ruleCache = ruleCache;
    }

    /**
     * Build promotion rule data (rule index + evaluate rules)
     * As shown in sequence diagram: buildPromotionRuleData()
     */
    public void buildPromotionRuleData() {
        logger.info("Starting to build promotion rule data");

        // Step 1: Find active promotion rules from repository
        List<PromotionRuleEntity> activeRules = promotionRuleRepository.findActivePromotionRules();
        logger.info("Found {} active promotion rules", activeRules.size());

        if (activeRules.isEmpty()) {
            logger.warn("No active rules found, clearing cache");
            ruleCache.clear();
            return;
        }

        // Step 2: Load conditions and actions for all active rules
        Map<Long, List<PromotionConditionEntity>> conditionsMap = activeRules.stream()
                .collect(Collectors.toMap(
                        PromotionRuleEntity::getId,
                        rule -> promotionConditionRepository.findByRuleId(rule.getId())
                ));

        Map<Long, List<PromotionActionEntity>> actionsMap = activeRules.stream()
                .collect(Collectors.toMap(
                        PromotionRuleEntity::getId,
                        rule -> promotionActionRepository.findByRuleId(rule.getId())
                ));

        // Step 3: Generate rule index using PromotionRuleIndexDomainService
        RuleIndex ruleIndex = promotionRuleIndexDomainService.generateRuleIndex(activeRules, conditionsMap);

        // Step 4: Save rule index to cache
        ruleCache.saveRuleIndex(ruleIndex);
        logger.info("Rule index saved to cache");

        // Step 5: Get promotion rules details using PromotionRuleDomainService
        Map<Long, EvaluatePromotionRule> evaluateRules =
                promotionRuleDomainService.getPromotionRulesDetails(activeRules, conditionsMap, actionsMap);

        // Step 6: Save evaluate promotion rules to cache
        ruleCache.saveEvaluatePromotionRules(evaluateRules);
        logger.info("Evaluate promotion rules saved to cache");

        logger.info("Promotion rule data build completed successfully");
    }

    public PagedPromotionRuleResponse getPromotionRules(Integer status, Long templateId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PromotionRuleEntity> entityPage = promotionRuleRepository.findByFilters(status, templateId, pageable);
        List<PromotionRuleSummaryResponse> items = entityPage.getContent().stream()
            .map(this::transformToPromotionRuleSummaryDTO)
            .collect(Collectors.toList());
        return new PagedPromotionRuleResponse(items, entityPage.getTotalElements(), page, size);
    }
    public PromotionRuleResponse getPromotionRuleDetail(Long ruleId) {
        PromotionRuleEntity entity = promotionRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("PromotionRule", ruleId));
        return transformToPromotionRuleDTO(entity);
    }
    private PromotionRuleResponse transformToPromotionRuleDTO(PromotionRuleEntity e) {
        PromotionRuleResponse r = new PromotionRuleResponse();
        r.setId(e.getId());
        r.setTemplateId(e.getTemplateId());
        r.setRuleName(e.getRuleName());
        r.setStartDate(e.getStartDate());
        r.setEndDate(e.getEndDate());
        r.setStatus(e.getStatus());
        r.setPriority(e.getPriority());
        r.setQuota(e.getQuota());
        r.setQuotaUsed(e.getQuotaUsed());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        r.setConditions(java.util.Collections.emptyList());
        r.setActions(java.util.Collections.emptyList());
        r.setStacking(null);
        return r;
    }
    private PromotionRuleSummaryResponse transformToPromotionRuleSummaryDTO(PromotionRuleEntity e) {
        PromotionRuleSummaryResponse r = new PromotionRuleSummaryResponse();
        r.setId(e.getId());
        r.setTemplateId(e.getTemplateId());
        r.setRuleName(e.getRuleName());
        r.setStartDate(e.getStartDate());
        r.setEndDate(e.getEndDate());
        r.setStatus(e.getStatus());
        r.setPriority(e.getPriority());
        r.setQuota(e.getQuota());
        r.setQuotaUsed(e.getQuotaUsed());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        return r;
    }
}
