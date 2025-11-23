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
    private Integer status; // 1=PENDING,2=ACTIVE,3=EXPIRED
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
        rule.status = 1; // PENDING by default until activated
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
    public boolean isActive() { return status != null && status == 2; }
    public boolean isExpired() { return status != null && status == 3 || LocalDateTime.now().isAfter(endDate); }
    public boolean isPending() { return status != null && status == 1; }

    public boolean isValidWindow() {
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    public boolean isCurrentlyApplicable() {
        return isActive() && isValidWindow() && !isExpired();
    }

    // Getters
    public Long getId() { return id; }
    public Long getTemplateId() { return templateId; }
    public String getRuleName() { return ruleName; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public Integer getStatus() { return status; }
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
    public void setStatus(Integer status) { this.status = status; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public void setQuota(String quota) { this.quota = quota; }
    public void setQuotaUsed(Integer quotaUsed) { this.quotaUsed = quotaUsed; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
