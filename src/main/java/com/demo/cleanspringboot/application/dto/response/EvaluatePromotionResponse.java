package com.demo.cleanspringboot.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for promotion evaluation
 * Source: ~/tasks/engine-evaluate-coupon.md
 */
public class EvaluatePromotionResponse {

    private String code;
    private EvaluationData data;

    public static class EvaluationData {
        @JsonProperty("eligible_rule_ids")
        private List<EligibleRuleDetail> eligibleRuleIds;

        @JsonProperty("evaluated_at")
        private LocalDateTime evaluatedAt;

        public List<EligibleRuleDetail> getEligibleRuleIds() { return eligibleRuleIds; }
        public void setEligibleRuleIds(List<EligibleRuleDetail> eligibleRuleIds) { this.eligibleRuleIds = eligibleRuleIds; }

        public LocalDateTime getEvaluatedAt() { return evaluatedAt; }
        public void setEvaluatedAt(LocalDateTime evaluatedAt) { this.evaluatedAt = evaluatedAt; }
    }

    public EvaluatePromotionResponse() {
        this.code = "success";
        this.data = new EvaluationData();
        this.data.setEvaluatedAt(LocalDateTime.now());
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public EvaluationData getData() { return data; }
    public void setData(EvaluationData data) { this.data = data; }
}

