package com.demo.cleanspringboot.domain.model;

import java.time.LocalDateTime;

/**
 * Domain model for PromotionRule (Aggregate Root)
 * Pure business logic - no Spring, no JPA annotations
 */
public class PromotionRule {

    private Long id;
    private Long templateId;
    private String ruleName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean active;
    private Integer priority;
    private String quota; // JSON string
    private Integer quotaUsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor for domain control
    private PromotionRule() {}

    // Factory method
    public static PromotionRule create(Long templateId, String ruleName,
                                      LocalDateTime startDate, LocalDateTime endDate) {
        PromotionRule rule = new PromotionRule();
        rule.templateId = templateId;
        rule.ruleName = ruleName;
        rule.startDate = startDate;
        rule.endDate = endDate;
        rule.active = true;
        rule.priority = 0;
        rule.quotaUsed = 0;
        rule.createdAt = LocalDateTime.now();
        rule.updatedAt = LocalDateTime.now();

        rule.validate();
        return rule;
    }

    // Business rule validation
    private void validate() {
        if (ruleName == null || ruleName.isBlank()) {
            throw new IllegalArgumentException("Rule name cannot be empty");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }

    // Business methods
    public boolean isActive() {
        return active != null && active;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(endDate);
    }

    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return isActive() && !now.isBefore(startDate) && !now.isAfter(endDate);
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

    // Public setters for reconstruction from persistence
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

