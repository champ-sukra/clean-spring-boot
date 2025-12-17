package com.demo.cleanspringboot.domain.service;

import com.demo.cleanspringboot.application.dto.request.EvaluatePromotionRequest;
import com.demo.cleanspringboot.domain.model.ConditionStore;
import com.demo.cleanspringboot.domain.model.ConditionType;
import com.demo.cleanspringboot.domain.model.EvaluatePromotionRule;
import com.demo.cleanspringboot.infrastructure.cache.RuleCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain Service for evaluating promotion rules against cart
 * Source: ~/sequence-diagram/evaluate-promotion.puml line 8
 * Participant: RuleEvaluationDomainService\n(domain/service)
 */
@Service
public class RuleEvaluationDomainService {

    private static final Logger logger = LoggerFactory.getLogger(RuleEvaluationDomainService.class);

    private final RuleCache ruleCache;

    public RuleEvaluationDomainService(RuleCache ruleCache) {
        this.ruleCache = ruleCache;
    }

    /**
     * Evaluate eligible rules against cart items
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 54-70
     *
     * @param carts Cart items to evaluate
     * @param sortedRuleIds Sorted rule IDs by priority
     * @param paymentMethod Payment method from request (optional)
     * @return List of eligible EvaluatePromotionRule
     */
    public List<EvaluatePromotionRule> evaluateEligibleRules(
            List<EvaluatePromotionRequest.CartItem> carts,
            List<Long> sortedRuleIds,
            String paymentMethod) {

        logger.info("Evaluating {} sorted rules against {} cart items with payment method: {}",
                    sortedRuleIds.size(), carts.size(), paymentMethod);

        List<EvaluatePromotionRule> eligibleRules = new ArrayList<>();

        // Loop for each ruleId in sortedRuleIds (line 56-68)
        for (Long ruleId : sortedRuleIds) {
            // Get EvaluatePromotionRule from cache (line 57-58)
            EvaluatePromotionRule rule = ruleCache.getEvaluatePromotionRule(ruleId);
            if (rule == null) {
                logger.warn("Rule {} not found in cache, skipping", ruleId);
                continue;
            }

            // Apply conditions (line 60)
            if (applyCondition(rule.getConditions(), carts, paymentMethod, rule)) {
                eligibleRules.add(rule);
            }
        }

        logger.info("Condition evaluation completed. {} eligible rules found before scope filtering", eligibleRules.size());

        // Apply mutual exclusion by scope (line 59-71)
        List<EvaluatePromotionRule> finalRules = deriveExclusiveByScope(eligibleRules);

        logger.info("Evaluation completed. {} final rules after deriveExclusiveByScope", finalRules.size());
        return finalRules;
    }

    /**
     * Apply condition check for a rule
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 60-72
     * Switch between condition types:
     * - PRODUCT: Check if cart items contains one from productCode list
     * - CATEGORY: Check if cart items contains one from categoryCode list
     * - QUANTITY: Check if cart items meet minimum quantity threshold using productCode or categoryCode list
     * - AMOUNT: Check if cart items meet minimum amount threshold using productCode or categoryCode list
     * - PAYMENT_METHOD: Check if payment method matches
     * - TOTAL_BILL: Calculate total bill = sum of all(item.quantity * item.price) if total meets minimum threshold
     */
    private boolean applyCondition(
            List<ConditionStore> conditions,
            List<EvaluatePromotionRequest.CartItem> carts,
            String paymentMethod,
            EvaluatePromotionRule rule) {

        // Rule must pass ALL conditions (AND logic)
        for (ConditionStore condition : conditions) {
            boolean passed = switch (condition.getType()) {
                case PRODUCT -> checkProductCondition(condition, carts);
                case CATEGORY -> checkCategoryCondition(condition, carts);
                case QUANTITY -> checkQuantityCondition(condition, carts);
                case AMOUNT -> checkAmountCondition(condition, carts);
                case PAYMENT_METHOD -> checkPaymentMethodCondition(condition, paymentMethod);
                case TOTAL_BILL -> checkTotalBillCondition(condition, carts);
                default -> true; // Other condition types not implemented yet
            };

            if (!passed) {
                logger.debug("Rule {} failed on condition type {}", rule.getRuleId(), condition.getType());
                return false;
            }
        }

        return true;
    }

    /**
     * Check PRODUCT condition
     * PRODUCT: Check if cart items contains one from productCode list
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 61
     */
    private boolean checkProductCondition(
            ConditionStore condition,
            List<EvaluatePromotionRequest.CartItem> carts) {

        List<String> productCodes = condition.getProductCodes();
        if (productCodes == null || productCodes.isEmpty()) {
            return true;
        }

        // Check if ANY cart item matches ANY product code
        boolean passed = carts.stream()
                .anyMatch(item -> productCodes.contains(item.getProductId()));

        logger.debug("PRODUCT check: required={}, passed={}", productCodes, passed);
        return passed;
    }

    /**
     * Check CATEGORY condition
     * CATEGORY: Check if cart items contains one from categoryCode list
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 62
     */
    private boolean checkCategoryCondition(
            ConditionStore condition,
            List<EvaluatePromotionRequest.CartItem> carts) {

        List<String> categoryCodes = condition.getCategoryCodes();
        if (categoryCodes == null || categoryCodes.isEmpty()) {
            return true;
        }

        // Check if ANY cart item matches ANY category code
        boolean passed = carts.stream()
                .anyMatch(item -> item.getCategoryId() != null && categoryCodes.contains(item.getCategoryId()));

        logger.debug("CATEGORY check: required={}, passed={}", categoryCodes, passed);
        return passed;
    }

    /**
     * Check QUANTITY condition
     * QUANTITY: Check if cart items meet minimum quantity threshold using productCode/categoryCode
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 63
     */
    private boolean checkQuantityCondition(
            ConditionStore condition,
            List<EvaluatePromotionRequest.CartItem> carts) {

        Integer requiredQty = condition.getBuyQty();
        if (requiredQty == null) {
            return true;
        }

        // Sum quantities from cart items matching product codes
        int totalQty = carts.stream()
                .filter(item -> condition.getProductCodes() != null &&
                               condition.getProductCodes().contains(item.getProductId()))
                .mapToInt(EvaluatePromotionRequest.CartItem::getQuantity)
                .sum();

        boolean passed = totalQty >= requiredQty;
        logger.debug("QUANTITY check: required={}, actual={}, passed={}", requiredQty, totalQty, passed);
        return passed;
    }

    /**
     * Check AMOUNT condition
     * AMOUNT: Check if cart items meet minimum amount threshold using productCode/categoryCode
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 64
     */
    private boolean checkAmountCondition(
            ConditionStore condition,
            List<EvaluatePromotionRequest.CartItem> carts) {

        Double requiredAmount = condition.getMinAmount();
        if (requiredAmount == null) {
            return true;
        }

        // Calculate total amount for matching products
        double totalAmount = carts.stream()
                .filter(item -> condition.getProductCodes() != null &&
                               condition.getProductCodes().contains(item.getProductId()))
                .mapToDouble(item -> item.getQuantity() * item.getPrice().doubleValue())
                .sum();

        boolean passed = totalAmount >= requiredAmount;
        logger.debug("AMOUNT check: required={}, actual={}, passed={}", requiredAmount, totalAmount, passed);
        return passed;
    }

    /**
     * Check PAYMENT_METHOD condition
     * PAYMENT_METHOD: Check if payment method matches
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 65
     */
    private boolean checkPaymentMethodCondition(ConditionStore condition, String paymentMethod) {
        List<String> allowedPaymentMethods = condition.getPaymentMethods();
        if (allowedPaymentMethods == null || allowedPaymentMethods.isEmpty()) {
            return true;
        }

        if (paymentMethod == null) {
            logger.debug("PAYMENT_METHOD check: payment method not provided, failed");
            return false;
        }

        boolean passed = allowedPaymentMethods.contains(paymentMethod);
        logger.debug("PAYMENT_METHOD check: required={}, actual={}, passed={}", allowedPaymentMethods, paymentMethod, passed);
        return passed;
    }

    /**
     * Check TOTAL_BILL condition
     * TOTAL_BILL: Calculate total bill = sum of all(item.quantity * item.price) if total meets minimum threshold
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 66
     */
    private boolean checkTotalBillCondition(
            ConditionStore condition,
            List<EvaluatePromotionRequest.CartItem> carts) {

        Double requiredAmount = condition.getMinAmount();
        if (requiredAmount == null) {
            return true;
        }

        // Calculate total bill: sum of ALL cart items (no product filter)
        double totalBill = carts.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice().doubleValue())
                .sum();

        boolean passed = totalBill >= requiredAmount;
        logger.debug("TOTAL_BILL check: required={}, actual={}, passed={}", requiredAmount, totalBill, passed);
        return passed;
    }

    /**
     * Derive exclusive rules by scope
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 59-71
     *
     * 1) Derive scopeKey on-the-fly from rule.conditions:
     *    - if any condition.type == TOTAL_BILL -> scopeKey = "ORDER"
     *    - else if condition.productCodes not empty -> scopeKey = "PRODUCT:{sku}" for each sku
     *    - else if condition.categoryCodes not empty -> scopeKey = "CATEGORY:{cat}" for each cat
     *    - ignore PAYMENT_METHOD / CHANNEL / SEGMENT for scope (they are filters)
     *
     * 2) Group by (rule.templateCode + scopeKey)
     *
     * 3) In each group keep highest priority rule only
     *    (this is default "mutual exclusion by scope", not stacking config)
     */
    private List<EvaluatePromotionRule> deriveExclusiveByScope(List<EvaluatePromotionRule> eligibleRules) {
        logger.info("Applying deriveExclusiveByScope to {} rules", eligibleRules.size());

        java.util.Map<String, EvaluatePromotionRule> scopeRuleMap = new java.util.HashMap<>();

        for (EvaluatePromotionRule rule : eligibleRules) {
            List<String> scopeKeys = deriveScopeKeys(rule);

            for (String scopeKey : scopeKeys) {
                String groupKey = rule.getTemplateCode() + ":" + scopeKey;

                EvaluatePromotionRule existing = scopeRuleMap.get(groupKey);
                if (existing == null || rule.getPriority() < existing.getPriority()) {
                    // Keep rule with lower priority number (higher priority)
                    scopeRuleMap.put(groupKey, rule);
                    logger.debug("Scope group [{}]: Keeping rule {} (priority={})",
                                groupKey, rule.getRuleId(), rule.getPriority());
                } else {
                    logger.debug("Scope group [{}]: Skipping rule {} (priority={}) - existing rule {} has higher priority ({})",
                                groupKey, rule.getRuleId(), rule.getPriority(), existing.getRuleId(), existing.getPriority());
                }
            }
        }

        // Get unique rules (deduplicate by ruleId)
        List<EvaluatePromotionRule> finalRules = scopeRuleMap.values().stream()
                .distinct()
                .toList();

        logger.info("After deriveExclusiveByScope: {} unique rules remain", finalRules.size());
        return finalRules;
    }

    /**
     * Derive scope keys from rule conditions
     */
    private List<String> deriveScopeKeys(EvaluatePromotionRule rule) {
        List<String> scopeKeys = new ArrayList<>();

        // Check for TOTAL_BILL condition
        boolean hasTotalBill = rule.getConditions().stream()
                .anyMatch(c -> c.getType() == ConditionType.TOTAL_BILL);

        if (hasTotalBill) {
            scopeKeys.add("ORDER");
            return scopeKeys;
        }

        // Check for product codes
        for (ConditionStore condition : rule.getConditions()) {
            if (condition.getProductCodes() != null && !condition.getProductCodes().isEmpty()) {
                for (String productCode : condition.getProductCodes()) {
                    scopeKeys.add("PRODUCT:" + productCode);
                }
            }
        }

        if (!scopeKeys.isEmpty()) {
            return scopeKeys;
        }

        // Check for category codes
        for (ConditionStore condition : rule.getConditions()) {
            if (condition.getCategoryCodes() != null && !condition.getCategoryCodes().isEmpty()) {
                for (String categoryCode : condition.getCategoryCodes()) {
                    scopeKeys.add("CATEGORY:" + categoryCode);
                }
            }
        }

        // If no scope derived, use a default
        if (scopeKeys.isEmpty()) {
            scopeKeys.add("GLOBAL");
        }

        return scopeKeys;
    }
}

