package com.demo.cleanspringboot.domain.model;

import java.util.*;

/**
 * Domain model for Rule Index
 * In-memory index structure for fast rule lookup
 */
public class RuleIndex {

    private final Map<String, Set<Long>> productRuleMap;
    private final Map<String, Set<Long>> categoryRuleMap;
    private final Map<String, Set<Long>> segmentRuleMap;
    private final Map<String, Set<Long>> paymentRuleMap;

    public RuleIndex() {
        this.productRuleMap = new HashMap<>();
        this.categoryRuleMap = new HashMap<>();
        this.segmentRuleMap = new HashMap<>();
        this.paymentRuleMap = new HashMap<>();
    }

    public void addProductRule(String productId, Long ruleId) {
        productRuleMap.computeIfAbsent(productId, k -> new HashSet<>()).add(ruleId);
    }

    public void addCategoryRule(String categoryId, Long ruleId) {
        categoryRuleMap.computeIfAbsent(categoryId, k -> new HashSet<>()).add(ruleId);
    }

    public void addSegmentRule(String segment, Long ruleId) {
        segmentRuleMap.computeIfAbsent(segment, k -> new HashSet<>()).add(ruleId);
    }

    public void addPaymentRule(String paymentMethod, Long ruleId) {
        paymentRuleMap.computeIfAbsent(paymentMethod, k -> new HashSet<>()).add(ruleId);
    }

    public Set<Long> getRulesByProduct(String productId) {
        return productRuleMap.getOrDefault(productId, Collections.emptySet());
    }

    public Set<Long> getRulesByCategory(String categoryId) {
        return categoryRuleMap.getOrDefault(categoryId, Collections.emptySet());
    }

    public Set<Long> getRulesBySegment(String segment) {
        return segmentRuleMap.getOrDefault(segment, Collections.emptySet());
    }

    public Set<Long> getRulesByPayment(String paymentMethod) {
        return paymentRuleMap.getOrDefault(paymentMethod, Collections.emptySet());
    }

    public Map<String, Set<Long>> getProductRuleMap() {
        return Collections.unmodifiableMap(productRuleMap);
    }

    public Map<String, Set<Long>> getCategoryRuleMap() {
        return Collections.unmodifiableMap(categoryRuleMap);
    }

    public Map<String, Set<Long>> getSegmentRuleMap() {
        return Collections.unmodifiableMap(segmentRuleMap);
    }

    public Map<String, Set<Long>> getPaymentRuleMap() {
        return Collections.unmodifiableMap(paymentRuleMap);
    }

    public void clear() {
        productRuleMap.clear();
        categoryRuleMap.clear();
        segmentRuleMap.clear();
        paymentRuleMap.clear();
    }
}

