package com.demo.cleanspringboot.domain.service;

import com.demo.cleanspringboot.application.dto.request.EvaluatePromotionRequest;
import com.demo.cleanspringboot.domain.model.*;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionActionEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionConditionEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionRuleEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Domain Service for transforming PromotionRule entities to EvaluatePromotionRule
 * As shown in sequence diagram: (domain/service)
 */
@Service
public class PromotionRuleDomainService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionRuleDomainService.class);
    private final ObjectMapper objectMapper;

    public PromotionRuleDomainService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Get promotion rules details and transform to EvaluatePromotionRule
     * Returns Map<RuleId, EvaluatePromotionRule>
     */
    public Map<Long, EvaluatePromotionRule> getPromotionRulesDetails(
            List<PromotionRuleEntity> activeRules,
            Map<Long, List<PromotionConditionEntity>> conditionsMap,
            Map<Long, List<PromotionActionEntity>> actionsMap) {

        logger.info("Transforming {} rules to EvaluatePromotionRule", activeRules.size());

        Map<Long, EvaluatePromotionRule> evaluateRulesMap = new HashMap<>();

        for (PromotionRuleEntity activeRule : activeRules) {
            Long ruleId = activeRule.getId();
            List<PromotionConditionEntity> conditions = conditionsMap.getOrDefault(ruleId, List.of());
            List<PromotionActionEntity> actions = actionsMap.getOrDefault(ruleId, List.of());

            EvaluatePromotionRule evaluateRule = transformToEvaluateRule(activeRule, conditions, actions);
            evaluateRulesMap.put(ruleId, evaluateRule);
        }

        logger.info("Transformation completed for {} rules", evaluateRulesMap.size());
        return evaluateRulesMap;
    }

    /**
     * Transform to EvaluatePromotionRule
     * Source: ~/epic.md
     * - **Condition Examples**
     * - **In-Memory Rule Detail Structure for Evaluation Engine**
     *
     * Fields:
     * - ruleId: INT
     * - priority: INT
     * - startDate: DATETIME
     * - endDate: DATETIME
     * - conditions: List<EvaluationCondition>
     * - actions: List<EvaluateAction>
     * - quota: QuotaDefinition
     * - quotaUsed: INT
     * - stacking: StackingConfig
     */
    private EvaluatePromotionRule transformToEvaluateRule(
            PromotionRuleEntity rule,
            List<PromotionConditionEntity> conditionEntities,
            List<PromotionActionEntity> actionEntities) {

        EvaluatePromotionRule evaluateRule = new EvaluatePromotionRule();
        evaluateRule.setRuleId(rule.getId());
        evaluateRule.setPriority(rule.getPriority());
        evaluateRule.setStartDate(rule.getStartDate());
        evaluateRule.setEndDate(rule.getEndDate());
        evaluateRule.setQuotaUsed(rule.getQuotaUsed());

        // Transform quota
        Quota quota = new Quota(rule.getQuota(), rule.getQuotaUsed());
        evaluateRule.setQuota(quota);

        // Transform conditions to ConditionStore (EvaluationCondition)
        List<ConditionStore> conditions = conditionEntities.stream()
                .map(this::transformToConditionStore)
                .collect(Collectors.toList());
        evaluateRule.setConditions(conditions);

        // Transform actions to ActionStore (EvaluateAction)
        List<ActionStore> actions = actionEntities.stream()
                .map(this::transformToActionStore)
                .collect(Collectors.toList());
        evaluateRule.setActions(actions);

        // Transform stacking configuration (if available)
        // TODO: Load stacking data from promotion_stacking table
        StackingConfig stacking = new StackingConfig();
        stacking.setCombinable(true); // Default for now
        evaluateRule.setStacking(stacking);

        return evaluateRule;
    }

    private ConditionStore transformToConditionStore(PromotionConditionEntity entity) {
        ConditionStore store = new ConditionStore();

        // Parse condition type from String to enum
        store.setType(ConditionType.fromString(entity.getConditionType()));

        // Parse threshold_value based on condition type
        // Source: ~/epic.md -- Condition Examples table
        // QUANTITY: threshold_value = 100 (direct number)
        // AMOUNT: threshold_value = 1000 (direct number)
        // PAYMENT_METHOD, SEGMENT: threshold_value = null
        if (entity.getThresholdValue() != null) {
            try {
                Double thresholdValue = Double.parseDouble(entity.getThresholdValue().toString());
                ConditionType type = store.getType();

                if (type == ConditionType.QUANTITY) {
                    store.setBuyQty(thresholdValue.intValue());
                } else if (type == ConditionType.AMOUNT) {
                    store.setMinAmount(thresholdValue);
                }
            } catch (Exception e) {
                logger.warn("Failed to parse threshold value: {}", entity.getThresholdValue(), e);
            }
        }

        // Parse product codes
        if (entity.getIncludeProductIds() != null) {
            store.setProductCodes(parseJsonToStringList(entity.getIncludeProductIds()));
        }

        // Parse category codes
        if (entity.getIncludeCategoryIds() != null) {
            store.setCategoryCodes(parseJsonToStringList(entity.getIncludeCategoryIds()));
        }

        // Parse attributes based on condition type
        // Source: ~/epic.md -- Condition Examples table
        // PAYMENT_METHOD: attributes = {"payment_methods": ["CREDIT_CARD", "DEBIT_CARD"]}
        // SEGMENT: attributes = {"segment": "FIRST_ORDER"}
        if (entity.getAttributes() != null) {
            try {
                Map<String, Object> attributes = parseJsonToMap(entity.getAttributes());

                if (attributes.containsKey("payment_methods")) {
                    Object pm = attributes.get("payment_methods");
                    if (pm instanceof List) {
                        store.setPaymentMethods((List<String>) pm);
                    }
                }

                if (attributes.containsKey("segment")) {
                    Object seg = attributes.get("segment");
                    if (seg instanceof String) {
                        store.setSegment((String) seg);
                    }
                }
            } catch (Exception e) {
                logger.warn("Failed to parse attributes: {}", entity.getAttributes(), e);
            }
        }

        return store;
    }

    private ActionStore transformToActionStore(PromotionActionEntity entity) {
        ActionStore store = new ActionStore();

        // Parse action type from String to enum
        store.setType(ActionType.fromString(entity.getActionType()));

        // Map discount_value based on action_type
        // Source: ~/epic.md -- promotion_action table & EvaluateAction structure
        // DISCOUNT_PERCENT: discount_value → discountPercent
        // FIXED_PRICE: discount_value → fixedPrice
        if (entity.getDiscountValue() != null) {
            ActionType type = store.getType();
            if (type == ActionType.DISCOUNT_PERCENT) {
                store.setDiscountPercent(entity.getDiscountValue().doubleValue());
            } else if (type == ActionType.FIXED_PRICE) {
                store.setFixedPrice(entity.getDiscountValue().doubleValue());
            }
        }

        // Parse reward items
        // Source: ~/epic.md line 98 -- reward_items: JSON array e.g ["PROD001"]
        if (entity.getRewardItems() != null) {
            store.setRewardProductCodes(parseJsonToStringList(entity.getRewardItems()));
        }

        // Parse attributes for reward_qty
        if (entity.getAttributes() != null) {
            try {
                Map<String, Object> attributes = parseJsonToMap(entity.getAttributes());
                if (attributes.containsKey("reward_qty")) {
                    store.setRewardQty(((Number) attributes.get("reward_qty")).intValue());
                }
            } catch (Exception e) {
                logger.warn("Failed to parse attributes: {}", entity.getAttributes(), e);
            }
            store.setAttributes(parseJsonToMap(entity.getAttributes()));
        }

        return store;
    }

    private List<String> parseJsonToStringList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            logger.warn("Failed to parse JSON to String list: {}", json, e);
            return List.of();
        }
    }

    private Map<String, Object> parseJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            logger.warn("Failed to parse JSON to Map: {}", json, e);
            return Map.of();
        }
    }

    /**
     * Evaluate eligible rules against cart items
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 55-63
     * Returns: List<EvaluatePromotionRule>
     */
    public List<EvaluatePromotionRule> evaluateEligibleRules(
            List<EvaluatePromotionRequest.CartItem> items,
            List<EvaluatePromotionRule> evaluateRules) {

        logger.info("Evaluating {} rules against {} cart items", evaluateRules.size(), items.size());

        List<EvaluatePromotionRule> eligibleRules = evaluateRules.stream()
                .filter(rule -> applyCondition(ConditionType.QUANTITY, items, rule))
                .collect(Collectors.toList());

        logger.info("Evaluation completed. {} eligible rules found", eligibleRules.size());
        return eligibleRules;
    }

    /**
     * Apply condition check for a rule
     * Source: ~/sequence-diagram/evaluate-promotion.puml line 56
     * Returns: true if rule is eligible, false otherwise
     */
    private boolean applyCondition(
            ConditionType conditionType,
            List<EvaluatePromotionRequest.CartItem> items,
            EvaluatePromotionRule rule) {

        if (conditionType != ConditionType.QUANTITY) {
            return false;
        }

        // Check QUANTITY conditions
        for (ConditionStore condition : rule.getConditions()) {
            if (condition.getType() == ConditionType.QUANTITY) {
                Integer requiredQty = condition.getBuyQty();
                if (requiredQty == null) {
                    continue;
                }

                // Sum quantities from cart items matching product codes
                int totalQty = items.stream()
                        .filter(item -> condition.getProductCodes() != null &&
                                       condition.getProductCodes().contains(item.getProductId()))
                        .mapToInt(EvaluatePromotionRequest.CartItem::getQuantity)
                        .sum();

                if (totalQty >= requiredQty) {
                    return true;
                }
            }
        }

        // If no QUANTITY condition found or not met, not eligible
        return false;
    }
}
