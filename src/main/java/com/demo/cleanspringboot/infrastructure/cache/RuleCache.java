package com.demo.cleanspringboot.infrastructure.cache;

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
     * Save evaluate promotion rules to in-memory cache
     */
    public void saveEvaluatePromotionRules(Map<Long, EvaluatePromotionRule> rules) {
        logger.info("Saving {} evaluate promotion rules to in-memory cache", rules.size());
        evaluatePromotionRules.clear();
        evaluatePromotionRules.putAll(rules);
        logger.info("Evaluate promotion rules saved successfully");
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
     * Find rule IDs by SKU
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 40
     */
    public List<Long> findRuleIdsBySku(String sku) {
        RuleIndex ruleIndex = getRuleIndex();
        return List.copyOf(ruleIndex.getProductRuleMap().getOrDefault(sku, Set.of()));
    }

    /**
     * Find rule IDs by payment method
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 43
     */
    public List<Long> findRuleIdsByPaymentMethod(String paymentMethod) {
        if (paymentMethod == null) {
            return List.of();
        }
        RuleIndex ruleIndex = getRuleIndex();
        return List.copyOf(ruleIndex.getPaymentRuleMap().getOrDefault(paymentMethod, Set.of()));
    }

    /**
     * Clear the cache
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

