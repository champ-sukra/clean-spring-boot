package com.demo.cleanspringboot.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for PromotionRule
 * Matches task specification: includes conditions, actions, stacking
 */
public class PromotionRuleResponse {

    private Long id;
    private Long templateId;
    private String ruleName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer status; // 1=PENDING,2=ACTIVE,3=EXPIRED
    private Integer priority;
    private Object quota;
    private Integer quotaUsed;
    private List<PromotionConditionResponse> conditions;
    private List<PromotionActionResponse> actions;
    private PromotionStackingResponse stacking;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public PromotionRuleResponse() {}

    // Getters
    public Long getId() { return id; }
    public Long getTemplateId() { return templateId; }
    public String getRuleName() { return ruleName; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public Integer getStatus() { return status; }
    public Integer getPriority() { return priority; }
    public Object getQuota() { return quota; }
    public Integer getQuotaUsed() { return quotaUsed; }
    public List<PromotionConditionResponse> getConditions() { return conditions; }
    public List<PromotionActionResponse> getActions() { return actions; }
    public PromotionStackingResponse getStacking() { return stacking; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public void setStatus(Integer status) { this.status = status; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public void setQuota(Object quota) { this.quota = quota; }
    public void setQuotaUsed(Integer quotaUsed) { this.quotaUsed = quotaUsed; }
    public void setConditions(List<PromotionConditionResponse> conditions) { this.conditions = conditions; }
    public void setActions(List<PromotionActionResponse> actions) { this.actions = actions; }
    public void setStacking(PromotionStackingResponse stacking) { this.stacking = stacking; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
