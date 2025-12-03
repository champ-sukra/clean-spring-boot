package com.demo.cleanspringboot.domain.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain model for evaluating promotion rules
 * Used in rule evaluation engine
 * As shown in sequence diagram notes:
 * - ruleId: int
 * - priority: int
 * - endDate: datetime
 * - conditions: []PromotionCondition
 * - actions: []PromotionAction
 * - quota_used: int
 * - quota: Quota
 */
public class EvaluatePromotionRule {

    private Long ruleId;
    private Integer priority;
    private LocalDateTime endDate;
    private List<PromotionCondition> conditions;
    private List<PromotionAction> actions;
    private Integer quotaUsed;
    private Quota quota;

    public EvaluatePromotionRule() {
    }

    public EvaluatePromotionRule(Long ruleId, Integer priority, LocalDateTime endDate,
                                  List<PromotionCondition> conditions, List<PromotionAction> actions,
                                  Integer quotaUsed, Quota quota) {
        this.ruleId = ruleId;
        this.priority = priority;
        this.endDate = endDate;
        this.conditions = conditions;
        this.actions = actions;
        this.quotaUsed = quotaUsed;
        this.quota = quota;
    }

    // Getters and Setters
    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<PromotionCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<PromotionCondition> conditions) {
        this.conditions = conditions;
    }

    public List<PromotionAction> getActions() {
        return actions;
    }

    public void setActions(List<PromotionAction> actions) {
        this.actions = actions;
    }

    public Integer getQuotaUsed() {
        return quotaUsed;
    }

    public void setQuotaUsed(Integer quotaUsed) {
        this.quotaUsed = quotaUsed;
    }

    public Quota getQuota() {
        return quota;
    }

    public void setQuota(Quota quota) {
        this.quota = quota;
    }
}

