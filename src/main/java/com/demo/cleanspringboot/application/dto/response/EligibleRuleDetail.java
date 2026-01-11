package com.demo.cleanspringboot.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for eligible rule detail in evaluation response
 * Source: ~/tasks/engine-evaluate-coupon.md
 */
public class EligibleRuleDetail {

    @JsonProperty("rule_id")
    private Long ruleId;

    @JsonProperty("rule_name")
    private String ruleName;

    @JsonProperty("coupon_code")
    private String couponCode;

    public EligibleRuleDetail() {
    }

    public EligibleRuleDetail(Long ruleId, String ruleName, String couponCode) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.couponCode = couponCode;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}

