package com.demo.cleanspringboot.application.dto.response;
import java.time.LocalDateTime;
public class PromotionRuleSummaryResponse {
    private Long id;
    private Long templateId;
    private String ruleName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer status;
    private Integer priority;
    private Object quota;
    private Integer quotaUsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Object getQuota() { return quota; }
    public void setQuota(Object quota) { this.quota = quota; }
    public Integer getQuotaUsed() { return quotaUsed; }
    public void setQuotaUsed(Integer quotaUsed) { this.quotaUsed = quotaUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
