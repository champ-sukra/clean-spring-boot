package com.demo.cleanspringboot.domain.model;

/**
 * Domain model for Promotion Condition
 */
public class PromotionCondition {

    private Long id;
    private String conditionType;
    private String includeProductIds;
    private String excludeProductIds;
    private String includeCategoryIds;
    private String attributes;

    public PromotionCondition() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getIncludeProductIds() {
        return includeProductIds;
    }

    public void setIncludeProductIds(String includeProductIds) {
        this.includeProductIds = includeProductIds;
    }

    public String getExcludeProductIds() {
        return excludeProductIds;
    }

    public void setExcludeProductIds(String excludeProductIds) {
        this.excludeProductIds = excludeProductIds;
    }

    public String getIncludeCategoryIds() {
        return includeCategoryIds;
    }

    public void setIncludeCategoryIds(String includeCategoryIds) {
        this.includeCategoryIds = includeCategoryIds;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}

