package com.demo.cleanspringboot.application.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for PromotionRule
 */
public class PromotionRuleResponse {

    private Long id;
    private Long templateId;
    private String ruleName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean active;
    private Integer priority;
    private String quota;
    private Integer quotaUsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public PromotionRuleResponse() {}

    // Builder-style setters for fluent construction
    public PromotionRuleResponse id(Long id) {
        this.id = id;
        return this;
    }

    public PromotionRuleResponse templateId(Long templateId) {
        this.templateId = templateId;
        return this;
    }

    public PromotionRuleResponse ruleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public PromotionRuleResponse startDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public PromotionRuleResponse endDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public PromotionRuleResponse active(Boolean active) {
        this.active = active;
        return this;
    }

    public PromotionRuleResponse priority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public PromotionRuleResponse quota(String quota) {
        this.quota = quota;
        return this;
    }

    public PromotionRuleResponse quotaUsed(Integer quotaUsed) {
        this.quotaUsed = quotaUsed;
        return this;
    }

    public PromotionRuleResponse createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public PromotionRuleResponse updatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    // Getters
    public Long getId() { return id; }
    public Long getTemplateId() { return templateId; }
    public String getRuleName() { return ruleName; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public Boolean getActive() { return active; }
    public Integer getPriority() { return priority; }
    public String getQuota() { return quota; }
    public Integer getQuotaUsed() { return quotaUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public void setActive(Boolean active) { this.active = active; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public void setQuota(String quota) { this.quota = quota; }
    public void setQuotaUsed(Integer quotaUsed) { this.quotaUsed = quotaUsed; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

