package com.demo.cleanspringboot.application.dto.response;
import java.util.List;
public class PromotionConditionResponse {
    private Long id;
    private Long ruleId;
    private String conditionType;
    private Double thresholdValue;
    private List<String> includeProductIds;
    private List<String> includeCategoryIds;
    private List<String> excludeProductIds;
    private Object attributes;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }
    public Double getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(Double thresholdValue) { this.thresholdValue = thresholdValue; }
    public List<String> getIncludeProductIds() { return includeProductIds; }
    public void setIncludeProductIds(List<String> includeProductIds) { this.includeProductIds = includeProductIds; }
    public List<String> getIncludeCategoryIds() { return includeCategoryIds; }
    public void setIncludeCategoryIds(List<String> includeCategoryIds) { this.includeCategoryIds = includeCategoryIds; }
    public List<String> getExcludeProductIds() { return excludeProductIds; }
    public void setExcludeProductIds(List<String> excludeProductIds) { this.excludeProductIds = excludeProductIds; }
    public Object getAttributes() { return attributes; }
    public void setAttributes(Object attributes) { this.attributes = attributes; }
}
