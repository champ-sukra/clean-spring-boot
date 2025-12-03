package com.demo.cleanspringboot.domain.service;

import com.demo.cleanspringboot.domain.model.*;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionActionEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionConditionEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionRuleEntity;
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

    /**
     * Get promotion rules details and transform to EvaluatePromotionRule
     * Returns Map<RuleId, EvaluatePromotionRule>
     */
    public Map<Long, EvaluatePromotionRule> getEvaluationPromotionRules(
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
     * As shown in sequence diagram notes:
     * - ruleId: int
     * - priority: int
     * - endDate: datetime
     * - conditions: []PromotionCondition
     * - actions: []PromotionAction
     * - quota_used: int
     * - quota: Quota
     */
    private EvaluatePromotionRule transformToEvaluateRule(
            PromotionRuleEntity rule,
            List<PromotionConditionEntity> conditionEntities,
            List<PromotionActionEntity> actionEntities) {

        EvaluatePromotionRule evaluateRule = new EvaluatePromotionRule();
        evaluateRule.setRuleId(rule.getId());
        evaluateRule.setPriority(rule.getPriority());
        evaluateRule.setEndDate(rule.getEndDate());
        evaluateRule.setQuotaUsed(rule.getQuotaUsed());

        // Transform quota
        Quota quota = new Quota(rule.getQuota(), rule.getQuotaUsed());
        evaluateRule.setQuota(quota);

        // Transform conditions
        List<PromotionCondition> conditions = conditionEntities.stream()
                .map(this::transformCondition)
                .collect(Collectors.toList());
        evaluateRule.setConditions(conditions);

        // Transform actions
        List<PromotionAction> actions = actionEntities.stream()
                .map(this::transformAction)
                .collect(Collectors.toList());
        evaluateRule.setActions(actions);

        return evaluateRule;
    }

    private PromotionCondition transformCondition(PromotionConditionEntity entity) {
        PromotionCondition condition = new PromotionCondition();
        condition.setId(entity.getId());
        condition.setConditionType(entity.getConditionType());
        condition.setIncludeProductIds(entity.getIncludeProductIds());
        condition.setExcludeProductIds(entity.getExcludeProductIds());
        condition.setIncludeCategoryIds(entity.getIncludeCategoryIds());
        condition.setAttributes(entity.getAttributes());
        return condition;
    }

    private PromotionAction transformAction(PromotionActionEntity entity) {
        PromotionAction action = new PromotionAction();
        action.setId(entity.getId());
        action.setActionType(entity.getActionType());
        action.setDiscountValue(entity.getDiscountValue());
        action.setAttributes(entity.getAttributes());
        return action;
    }
}

