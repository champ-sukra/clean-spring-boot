package com.demo.cleanspringboot.application.service;

import com.demo.cleanspringboot.application.dto.request.EvaluatePromotionRequest;
import com.demo.cleanspringboot.domain.model.EvaluatePromotionRule;
import com.demo.cleanspringboot.domain.service.PromotionRuleDomainService;
import com.demo.cleanspringboot.infrastructure.cache.RuleCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Application Service for Promotion Evaluation
 * Source: ~/sequence-diagram/evaluate-promotion.puml
 * Participant: PromotionEvaluationService\n(application/service)
 */
@Service
public class PromotionEvaluationService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionEvaluationService.class);

    private final RuleCache ruleCache;
    private final PromotionRuleDomainService domainService;

    public PromotionEvaluationService(RuleCache ruleCache, PromotionRuleDomainService domainService) {
        this.ruleCache = ruleCache;
        this.domainService = domainService;
    }

    /**
     * Evaluate promotion rules for cart items
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 33
     */
    public List<Long> evaluatePromotionRules(EvaluatePromotionRequest request) {
        logger.info("Starting promotion evaluation for cartId: {}", request.getCartId());

        // Step 1: Loop for each cart item - find rule IDs by SKU (line 40-42)
        Set<Long> allRuleIds = new HashSet<>();
        for (EvaluatePromotionRequest.CartItem item : request.getItems()) {
            List<Long> ruleIds = ruleCache.findRuleIdsBySku(item.getProductId());
            allRuleIds.addAll(ruleIds);
            logger.debug("Found {} rules for SKU: {}", ruleIds.size(), item.getProductId());
        }

        // Step 2: Find rule IDs by payment method (line 43-44) - skip if null
        if (request.getPaymentMethod() != null) {
            List<Long> paymentRuleIds = ruleCache.findRuleIdsByPaymentMethod(request.getPaymentMethod());
            allRuleIds.addAll(paymentRuleIds);
            logger.debug("Found {} rules for payment method: {}", paymentRuleIds.size(), request.getPaymentMethod());
        }

        // Step 3: Filter rule IDs (line 46-50)
        List<Long> filteredRuleIds = filterRuleIds(allRuleIds);
        logger.info("Filtered to {} eligible rules", filteredRuleIds.size());

        // Step 4: Get EvaluatePromotionRule for each filtered rule ID (line 51-54)
        List<EvaluatePromotionRule> evaluateRules = new ArrayList<>();
        for (Long ruleId : filteredRuleIds) {
            EvaluatePromotionRule rule = ruleCache.getEvaluatePromotionRule(ruleId);
            if (rule != null) {
                evaluateRules.add(rule);
            }
        }

        // Step 5: Evaluate eligible rules via domain service (line 55-60)
        List<EvaluatePromotionRule> eligibleRules = domainService.evaluateEligibleRules(request.getItems(), evaluateRules);

        // Step 6: Transform to result (line 66)
        List<Long> eligibleRuleIds = transformToEvaluatePromotionRuleResult(eligibleRules);

        logger.info("Evaluation complete. {} eligible rules found", eligibleRuleIds.size());
        return eligibleRuleIds;
    }

    /**
     * Transform to EvaluatePromotionRuleResult
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 66
     */
    private List<Long> transformToEvaluatePromotionRuleResult(List<EvaluatePromotionRule> eligibleRules) {
        return eligibleRules.stream()
                .map(EvaluatePromotionRule::getRuleId)
                .collect(Collectors.toList());
    }

    /**
     * Filter rule IDs
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 46-50
     * - filter by current time (end)
     * - deduplicate ruleIds
     */
    private List<Long> filterRuleIds(Set<Long> ruleIds) {
        LocalDateTime now = LocalDateTime.now();

        return ruleIds.stream()
                .distinct()  // Deduplicate
                .filter(ruleId -> {
                    EvaluatePromotionRule rule = ruleCache.getEvaluatePromotionRule(ruleId);
                    if (rule == null) {
                        return false;
                    }
                    // Filter by current time (must be before end date)
                    return rule.getEndDate() == null || rule.getEndDate().isAfter(now);
                })
                .collect(Collectors.toList());
    }
}

