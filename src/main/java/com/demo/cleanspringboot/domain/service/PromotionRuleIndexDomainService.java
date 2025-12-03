package com.demo.cleanspringboot.domain.service;

import com.demo.cleanspringboot.domain.model.RuleIndex;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionConditionEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionRuleEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Domain Service for generating Rule Index
 * As shown in sequence diagram: (domain/service)
 */
@Service
public class PromotionRuleIndexDomainService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionRuleIndexDomainService.class);
    private final ObjectMapper objectMapper;

    public PromotionRuleIndexDomainService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Generate rule index from active promotion rules
     * Extracts product IDs, category IDs, segments, and payment methods
     */
    public RuleIndex generateRuleIndex(
            List<PromotionRuleEntity> rules,
            Map<Long, List<PromotionConditionEntity>> conditionsMap) {

        logger.info("Starting rule index generation for {} rules", rules.size());

        RuleIndex ruleIndex = new RuleIndex();

        for (PromotionRuleEntity rule : rules) {
            Long ruleId = rule.getId();
            List<PromotionConditionEntity> conditions = conditionsMap.getOrDefault(ruleId, List.of());

            extractRuleIndex(ruleIndex, ruleId, conditions);
        }

        logger.info("Rule index generation completed");
        logIndexStatistics(ruleIndex);

        return ruleIndex;
    }

    /**
     * Extract rule index from conditions
     * Constructs in-memory maps for:
     * - product: "P-1001" → [1,5]
     * - category: "C-2001" → [2,7]
     * - segment: FIRST_ORDER → [8]
     * - payment: VISA → [4]
     */
    private void extractRuleIndex(RuleIndex ruleIndex, Long ruleId, List<PromotionConditionEntity> conditions) {
        for (PromotionConditionEntity condition : conditions) {
            String conditionType = condition.getConditionType();

            // Extract product IDs (as Strings)
            if (condition.getIncludeProductIds() != null) {
                List<String> productIds = parseJsonToStringList(condition.getIncludeProductIds());
                for (String productId : productIds) {
                    ruleIndex.addProductRule(productId, ruleId);
                }
            }

            // Extract category IDs (as Strings)
            if (condition.getIncludeCategoryIds() != null) {
                List<String> categoryIds = parseJsonToStringList(condition.getIncludeCategoryIds());
                for (String categoryId : categoryIds) {
                    ruleIndex.addCategoryRule(categoryId, ruleId);
                }
            }

            // Extract customer segments
            if ("CUSTOMER_SEGMENT".equals(conditionType) && condition.getAttributes() != null) {
                Map<String, Object> attributes = parseJsonToMap(condition.getAttributes());
                String segment = (String) attributes.get("segment");
                if (segment != null) {
                    ruleIndex.addSegmentRule(segment, ruleId);
                }
            }

            // Extract payment methods
            if ("PAYMENT_METHOD".equals(conditionType) && condition.getAttributes() != null) {
                Map<String, Object> attributes = parseJsonToMap(condition.getAttributes());
                String paymentMethod = (String) attributes.get("payment_method");
                if (paymentMethod != null) {
                    ruleIndex.addPaymentRule(paymentMethod, ruleId);
                }
            }
        }
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

    private void logIndexStatistics(RuleIndex ruleIndex) {
        logger.info("Rule Index Statistics:");
        logger.info("  - Products indexed: {}", ruleIndex.getProductRuleMap().size());
        logger.info("  - Categories indexed: {}", ruleIndex.getCategoryRuleMap().size());
        logger.info("  - Segments indexed: {}", ruleIndex.getSegmentRuleMap().size());
        logger.info("  - Payment methods indexed: {}", ruleIndex.getPaymentRuleMap().size());
    }
}

