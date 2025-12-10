package com.demo.cleanspringboot.domain.model;

import java.util.List;

/**
 * Domain model for promotion evaluation result
 */
public class PromotionEvaluationResult {

    private Long ruleId;
    private Boolean eligible;
    private String reason;

    public PromotionEvaluationResult(Long ruleId, Boolean eligible, String reason) {
        this.ruleId = ruleId;
        this.eligible = eligible;
        this.reason = reason;
    }

    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }

    public Boolean getEligible() { return eligible; }
    public void setEligible(Boolean eligible) { this.eligible = eligible; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

