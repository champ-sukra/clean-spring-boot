package com.demo.cleanspringboot.domain.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain model for evaluating promotion rules
 * Used in rule evaluation engine
 * Source: ~/epic.md -- **In-Memory Rule Detail Structure for Evaluation Engine**
 *
 * EvaluateRule structure:
 * - ruleId: INT - Unique rule identifier
 * - priority: INT - Rule priority (lower = higher priority)
 * - startDate: DATETIME - Rule active start
 * - endDate: DATETIME - Rule active end
 * - conditions: List<EvaluationCondition> - List of eligibility conditions
 * - actions: List<EvaluateAction> - List of reward actions
 * - quota: QuotaDefinition - Quota limits for rule usage
 * - quotaUsed: INT - Current global usage count
 * - stacking: StackingConfig - Stacking / combinability settings
 */
public class EvaluatePromotionRule {

    private Long ruleId;
    private String templateCode;
    private Integer priority;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<ConditionStore> conditions;
    private List<ActionStore> actions;
    private Quota quota;
    private Integer quotaUsed;
    private StackingConfig stacking;

    public EvaluatePromotionRule() {
    }

    public EvaluatePromotionRule(Long ruleId, Integer priority, LocalDateTime startDate, LocalDateTime endDate,
                                  List<ConditionStore> conditions, List<ActionStore> actions,
                                  Quota quota, Integer quotaUsed, StackingConfig stacking) {
        this.ruleId = ruleId;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.conditions = conditions;
        this.actions = actions;
        this.quota = quota;
        this.quotaUsed = quotaUsed;
        this.stacking = stacking;
    }

    // Getters and Setters
    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<ConditionStore> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionStore> conditions) {
        this.conditions = conditions;
    }

    public List<ActionStore> getActions() {
        return actions;
    }

    public void setActions(List<ActionStore> actions) {
        this.actions = actions;
    }

    public Quota getQuota() {
        return quota;
    }

    public void setQuota(Quota quota) {
        this.quota = quota;
    }

    public Integer getQuotaUsed() {
        return quotaUsed;
    }

    public void setQuotaUsed(Integer quotaUsed) {
        this.quotaUsed = quotaUsed;
    }

    public StackingConfig getStacking() {
        return stacking;
    }

    public void setStacking(StackingConfig stacking) {
        this.stacking = stacking;
    }
}

