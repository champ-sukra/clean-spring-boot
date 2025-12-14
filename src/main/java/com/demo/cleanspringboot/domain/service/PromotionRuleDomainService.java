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
     * Build evaluate rules from active rules
     * Source: ~/sequence-diagram/create-rule-index.puml line 72-82
     * Source: ~/epic.md - In-Memory Rule Detail Structure for Evaluation Engine
     * ConditionType including PRODUCT, QUANTITY, AMOUNT, CATEGORY, TOTAL_BILL,
     * PAYMENT_METHOD, CUSTOMER_SEGMENT, FIRST_ORDER, CHANNEL, BRAND, SHIPPING_METHOD
     * Returns Map<ruleId, EvaluatePromotionRule>
     */
    public Map<Long, EvaluatePromotionRule> buildEvaluateRules(
            List<PromotionRuleEntity> activeRules,
            Map<Long, List<PromotionConditionEntity>> conditionsMap,
            Map<Long, List<PromotionActionEntity>> actionsMap) {

        logger.info("Building evaluate rules from {} active rules", activeRules.size());

        Map<Long, EvaluatePromotionRule> evaluateRulesMap = new HashMap<>();

        // Loop for each activeRule (sequence diagram line 74-79)
        for (PromotionRuleEntity activeRule : activeRules) {
            Long ruleId = activeRule.getId();
            List<PromotionConditionEntity> conditions = conditionsMap.getOrDefault(ruleId, List.of());
            List<PromotionActionEntity> actions = actionsMap.getOrDefault(ruleId, List.of());

            // transformToEvaluateRule (sequence diagram line 75)
            EvaluatePromotionRule evaluateRule = transformToEvaluateRule(activeRule, conditions, actions);
            evaluateRulesMap.put(ruleId, evaluateRule);
        }

        logger.info("Build completed. {} evaluate rules created", evaluateRulesMap.size());
        return evaluateRulesMap;
    }

    /**
     * Transform to EvaluatePromotionRule
     * Source: ~/sequence-diagram/create-rule-index.puml line 75-80
     * Source: ~/epic.md
     * - **In-Memory Rule Detail Structure for Evaluation Engine**
     * - ConditionType including QUANTITY, AMOUNT, TOTAL_BILL, PRODUCT, CATEGORY,
     *   PAYMENT_METHOD, CUSTOMER_SEGMENT, FIRST_ORDER, CHANNEL, BRAND, SHIPPING_METHOD
     * - **Sample Records → promotion_condition**
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

    /**
     * Transform PromotionConditionEntity to ConditionStore
     * Source: ~/epic.md - Sample Records → promotion_condition
     * Handles all condition types: PRODUCT, QUANTITY, AMOUNT, CATEGORY, TOTAL_BILL,
     * PAYMENT_METHOD, CUSTOMER_SEGMENT, SHIPPING_METHOD, CHANNEL, BRAND
     */
    private ConditionStore transformToConditionStore(PromotionConditionEntity entity) {
        ConditionStore store = new ConditionStore();

        // Parse condition type from String to enum
        ConditionType type = ConditionType.fromString(entity.getConditionType());

        // Parse threshold_value based on condition type
        // Source: ~/epic.md -- Sample Records → promotion_condition
        // QUANTITY: threshold_value = 100 (direct number)
        // AMOUNT: threshold_value = 1000 (direct number)
        // TOTAL_BILL: threshold_value = 1000 (direct number)
        // PRODUCT, CATEGORY, PAYMENT_METHOD, CUSTOMER_SEGMENT: threshold_value = null
        if (entity.getThresholdValue() != null) {
            try {
                Double thresholdValue = Double.parseDouble(entity.getThresholdValue());

                if (type == ConditionType.QUANTITY) {
                    store.setBuyQty(thresholdValue.intValue());
                } else if (type == ConditionType.AMOUNT || type == ConditionType.TOTAL_BILL) {
                    store.setMinAmount(thresholdValue);
                }
            } catch (Exception e) {
                logger.warn("Failed to parse threshold value: {}", entity.getThresholdValue(), e);
            }
        }

        // Parse product codes - used by PRODUCT, QUANTITY, AMOUNT conditions
        // Source: ~/epic.md - EvaluationCondition: productCodes for PRODUCT, QUANTITY, AMOUNT
        if (entity.getIncludeProductIds() != null) {
            store.setProductCodes(parseJsonToStringList(entity.getIncludeProductIds()));
        }

        // Parse category codes - used by CATEGORY, QUANTITY, AMOUNT conditions
        // Source: ~/epic.md - EvaluationCondition: categoryCodes for CATEGORY, QUANTITY, AMOUNT
        if (entity.getIncludeCategoryIds() != null) {
            store.setCategoryCodes(parseJsonToStringList(entity.getIncludeCategoryIds()));
        }

        // Set the condition type
        store.setType(type);

        // Parse attributes based on condition type
        // Source: ~/epic.md -- Sample Records → promotion_condition
        // PAYMENT_METHOD: attributes = {"payment_methods": ["CREDIT_CARD", "DEBIT_CARD"]}
        // CUSTOMER_SEGMENT: attributes = {"segment": "FIRST_ORDER"}
        // SHIPPING_METHOD: attributes = {"shipping_methods": ["EXPRESS", "STANDARD"]}
        // CHANNEL: attributes = {"channels": ["MOBILE", "WEB"]}
        // BRAND: attributes = {"brands": ["BRAND_A", "BRAND_B"]}
        if (entity.getAttributes() != null) {
            try {
                Map<String, Object> attributes = parseJsonToMap(entity.getAttributes());

                // Handle payment methods
                if (attributes.containsKey("payment_methods")) {
                    Object pm = attributes.get("payment_methods");
                    if (pm instanceof List) {
                        store.setPaymentMethods((List<String>) pm);
                    }
                }

                // Handle customer segment
                if (attributes.containsKey("segment")) {
                    Object seg = attributes.get("segment");
                    if (seg instanceof String) {
                        store.setSegment((String) seg);
                    }
                }

                // Handle shipping methods
                if (attributes.containsKey("shipping_methods")) {
                    Object sm = attributes.get("shipping_methods");
                    if (sm instanceof List) {
                        store.setShippingMethods((List<String>) sm);
                    }
                }

                // Handle channels
                if (attributes.containsKey("channels")) {
                    Object ch = attributes.get("channels");
                    if (ch instanceof List) {
                        store.setChannels((List<String>) ch);
                    }
                }

                // Handle brands
                if (attributes.containsKey("brands")) {
                    Object br = attributes.get("brands");
                    if (br instanceof List) {
                        store.setBrands((List<String>) br);
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
}
