package com.demo.cleanspringboot.application.service;

import com.demo.cleanspringboot.application.dto.request.EvaluatePromotionRequest;
import com.demo.cleanspringboot.domain.model.ConditionType;
import com.demo.cleanspringboot.domain.model.EvaluatePromotionRule;
import com.demo.cleanspringboot.domain.service.RuleEvaluationDomainService;
import com.demo.cleanspringboot.infrastructure.cache.RuleCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Application Service for Promotion Evaluation
 * Source: ~/sequence-diagram/evaluate-promotion.puml line 7
 * Participant: PromotionEvaluationService\n(application/service)
 */
@Service
public class PromotionEvaluationService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionEvaluationService.class);

    private final RuleCache ruleCache;
    private final RuleEvaluationDomainService domainService;

    public PromotionEvaluationService(RuleCache ruleCache, RuleEvaluationDomainService domainService) {
        this.ruleCache = ruleCache;
        this.domainService = domainService;
    }

    /**
     * Evaluate promotion rules for cart items
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 33
     */
    public List<Long> evaluatePromotionRules(EvaluatePromotionRequest request) {
        long startTime = System.nanoTime();
        logger.info("Starting promotion evaluation for cartId: {}", request.getCartId());

        // Step 1: Loop for each cart item - find rule IDs by productId and categoryId (line 37-42)
        long step1Start = System.nanoTime();
        Set<Long> allRuleIds = new HashSet<>();
        for (EvaluatePromotionRequest.CartItem item : request.getItems()) {
            // Find by product ID (line 38-39)
            List<Long> productRuleIds = ruleCache.findRuleIdsBySku(item.getProductId());
            allRuleIds.addAll(productRuleIds);
            logger.debug("Found {} rules for productId: {}", productRuleIds.size(), item.getProductId());

            // Find by category ID (line 41-42)
            if (item.getCategoryId() != null) {
                List<Long> categoryRuleIds = ruleCache.findRuleIdsByCategoryId(item.getCategoryId());
                allRuleIds.addAll(categoryRuleIds);
                logger.debug("Found {} rules for categoryId: {}", categoryRuleIds.size(), item.getCategoryId());
            }
        }
        long step1Time = (System.nanoTime() - step1Start) / 1_000_000;

        // Step 2: Find rule IDs by payment method (line 44-45) - skip if null
        long step2Start = System.nanoTime();
        if (request.getPaymentMethod() != null) {
            List<Long> paymentRuleIds = ruleCache.findRuleIdsByPaymentMethod(request.getPaymentMethod());
            allRuleIds.addAll(paymentRuleIds);
            logger.debug("Found {} rules for payment method: {}", paymentRuleIds.size(), request.getPaymentMethod());
        }

        // Step 3: Find rule IDs by condition type TOTAL_BILL (line 47-48)
        List<Long> totalBillRuleIds = ruleCache.findRuleIdsByCondition(ConditionType.TOTAL_BILL);
        allRuleIds.addAll(totalBillRuleIds);
        logger.debug("Found {} rules for TOTAL_BILL condition", totalBillRuleIds.size());
        long step2Time = (System.nanoTime() - step2Start) / 1_000_000;

        // Step 4: Arrange rule IDs (line 50-54)
        long step4Start = System.nanoTime();
        List<Long> sortedRuleIds = arrangeRuleIds(allRuleIds);
        long step4Time = (System.nanoTime() - step4Start) / 1_000_000;
        logger.info("Arranged to {} sorted rules in {}ms", sortedRuleIds.size(), step4Time);

        // Step 5: Evaluate eligible rules via domain service (line 56)
        long step5Start = System.nanoTime();
        List<EvaluatePromotionRule> eligibleRules = domainService.evaluateEligibleRules(
                request.getItems(),
                sortedRuleIds,
                request.getPaymentMethod());
        long step5Time = (System.nanoTime() - step5Start) / 1_000_000;

        // Step 6: Transform to result (line 73)
        long step6Start = System.nanoTime();
        List<Long> eligibleRuleIds = transformToEvaluatePromotionRuleResult(eligibleRules);
        long step6Time = (System.nanoTime() - step6Start) / 1_000_000;

        long totalTime = (System.nanoTime() - startTime) / 1_000_000;
        logger.info("Evaluation complete in {}ms. Breakdown: lookup={}ms, arrange={}ms, evaluate={}ms, transform={}ms. {} eligible rules found",
                    totalTime, step1Time + step2Time, step4Time, step5Time, step6Time, eligibleRuleIds.size());
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
     * Arrange rule IDs
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 45-51
     * - filter out by current time (end)
     * - deduplicate ruleIds
     * - sort by priority ASC by rule.priority
     */
    private List<Long> arrangeRuleIds(Set<Long> ruleIds) {
        LocalDateTime now = LocalDateTime.now();

        return ruleIds.stream()
                .distinct()  // Deduplicate
                .filter(ruleId -> {
                    EvaluatePromotionRule rule = ruleCache.getEvaluatePromotionRule(ruleId);
                    if (rule == null) {
                        return false;
                    }
                    // Filter out by current time (must be before end date)
                    return rule.getEndDate() == null || rule.getEndDate().isAfter(now);
                })
                .sorted((id1, id2) -> {
                    // Sort by priority ASC (lower priority value = higher priority)
                    EvaluatePromotionRule rule1 = ruleCache.getEvaluatePromotionRule(id1);
                    EvaluatePromotionRule rule2 = ruleCache.getEvaluatePromotionRule(id2);
                    if (rule1 == null || rule2 == null) {
                        return 0;
                    }
                    return Integer.compare(rule1.getPriority(), rule2.getPriority());
                })
                .collect(Collectors.toList());
    }
}

