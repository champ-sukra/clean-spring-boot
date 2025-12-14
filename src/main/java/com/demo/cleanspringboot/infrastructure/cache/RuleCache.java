package com.demo.cleanspringboot.infrastructure.cache;

import com.demo.cleanspringboot.domain.model.ConditionType;
import com.demo.cleanspringboot.domain.model.EvaluatePromotionRule;
import com.demo.cleanspringboot.domain.model.RuleIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * In-Memory Cache for Rule Index and Evaluate Promotion Rules
 * Source: ~/sequence-diagram/evaluate-promotion.puml line 9
 * Source: ~/sequence-diagram/create-rule-index.puml line 8
 * Participant: RuleCache\n(infrastructure/adapter/cache)
 *
 * Store into local in-memory:
 *   ruleIndex.productRuleMap
 *   ruleIndex.categoryRuleMap
 *   ruleIndex.segmentRuleMap
 *   ruleIndex.paymentRuleMap
 *   ruleIndex.globalRuleMap (global: "TOTAL_BILL" → [15, 20])
 *   evaluatePromotionRules (Map<RuleId, EvaluatePromotionRule>)
 *
 * Available for Evaluate() instantly
 */
@Component
public class RuleCache {

    private static final Logger logger = LoggerFactory.getLogger(RuleCache.class);

    private final AtomicReference<RuleIndex> ruleIndexRef = new AtomicReference<>();
    private final Map<Long, EvaluatePromotionRule> evaluatePromotionRules = new ConcurrentHashMap<>();

    /**
     * Save rule index to in-memory cache
     */
    public void saveRuleIndex(RuleIndex ruleIndex) {
        logger.info("Saving rule index to in-memory cache");
        ruleIndexRef.set(ruleIndex);
        logger.info("Rule index saved successfully");
    }

    /**
     * Get current rule index from cache
     */
    public RuleIndex getRuleIndex() {
        RuleIndex ruleIndex = ruleIndexRef.get();
        if (ruleIndex == null) {
            logger.warn("Rule index not found in cache, returning empty index");
            return new RuleIndex();
        }
        return ruleIndex;
    }

    /**
     * Save evaluate rules to in-memory cache
     * Source: ~/sequence-diagram/create-rule-index.puml line 85
     */
    public void saveEvaluateRules(Map<Long, EvaluatePromotionRule> rules) {
        logger.info("Saving {} evaluate rules to in-memory cache", rules.size());
        evaluatePromotionRules.clear();
        evaluatePromotionRules.putAll(rules);
        logger.info("Evaluate rules saved successfully");
    }

    /**
     * Get evaluate promotion rules from cache
     */
    public Map<Long, EvaluatePromotionRule> getEvaluatePromotionRules() {
        return new ConcurrentHashMap<>(evaluatePromotionRules);
    }

    /**
     * Get a specific evaluate promotion rule by ID
     */
    public EvaluatePromotionRule getEvaluatePromotionRule(Long ruleId) {
        return evaluatePromotionRules.get(ruleId);
    }

    /**
     * Find rule IDs by SKU (product ID)
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 38-39
     */
    public List<Long> findRuleIdsBySku(String sku) {
        RuleIndex ruleIndex = getRuleIndex();
        return List.copyOf(ruleIndex.getProductRuleMap().getOrDefault(sku, Set.of()));
    }

    /**
     * Find rule IDs by category ID
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 41-42
     */
    public List<Long> findRuleIdsByCategoryId(String categoryId) {
        if (categoryId == null) {
            logger.debug("Category ID is null, returning empty list");
            return List.of();
        }
        RuleIndex ruleIndex = getRuleIndex();
        Set<Long> ruleIds = ruleIndex.getCategoryRuleMap().getOrDefault(categoryId, Set.of());

        logger.debug("Looking up categoryId: '{}', found {} rules", categoryId, ruleIds.size());
        if (ruleIds.isEmpty()) {
            logger.debug("No rules found for categoryId: '{}'. Available categories: {}",
                        categoryId, ruleIndex.getCategoryRuleMap().keySet());
        }

        return List.copyOf(ruleIds);
    }

    /**
     * Find rule IDs by payment method
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 44-45
     */
    public List<Long> findRuleIdsByPaymentMethod(String paymentMethod) {
        if (paymentMethod == null) {
            return List.of();
        }
        RuleIndex ruleIndex = getRuleIndex();
        return List.copyOf(ruleIndex.getPaymentRuleMap().getOrDefault(paymentMethod, Set.of()));
    }

    /**
     * Find rule IDs by condition type
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 47-48
     * Used to find all rules with specific condition types (e.g., TOTAL_BILL)
     * Uses globalRuleMap index for fast lookup
     */
    public List<Long> findRuleIdsByCondition(ConditionType conditionType) {
        if (conditionType == null) {
            return List.of();
        }

        logger.debug("Finding rule IDs for condition type: {}", conditionType);

        RuleIndex ruleIndex = getRuleIndex();
        return List.copyOf(ruleIndex.getRulesByGlobal(conditionType.name()));
    }


    /**
     * Clear all cache
     */
    public void clear() {
        logger.info("Clearing rule index cache and evaluate promotion rules");
        ruleIndexRef.set(null);
        evaluatePromotionRules.clear();
    }

    /**
     * Check if cache is initialized
     */
    public boolean isInitialized() {
        return ruleIndexRef.get() != null && !evaluatePromotionRules.isEmpty();
    }
}

