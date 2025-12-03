package com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

/**
 * JPA Entity for promotion_condition table
 */
@Entity
@Table(name = "promotion_condition")
public class PromotionConditionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_id", nullable = false)
    private Long ruleId;

    @Column(name = "condition_type")
    private String conditionType;

    @Column(name = "threshold_value")
    private String thresholdValue;

    @Column(name = "include_product_ids", columnDefinition = "JSON")
    private String includeProductIds;

    @Column(name = "include_category_ids", columnDefinition = "JSON")
    private String includeCategoryIds;

    @Column(name = "exclude_product_ids", columnDefinition = "JSON")
    private String excludeProductIds;

    @Column(name = "attributes", columnDefinition = "JSON")
    private String attributes;

    public PromotionConditionEntity() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }

    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }

    public String getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(String thresholdValue) { this.thresholdValue = thresholdValue; }

    public String getIncludeProductIds() { return includeProductIds; }
    public void setIncludeProductIds(String includeProductIds) { this.includeProductIds = includeProductIds; }

    public String getIncludeCategoryIds() { return includeCategoryIds; }
    public void setIncludeCategoryIds(String includeCategoryIds) { this.includeCategoryIds = includeCategoryIds; }

    public String getExcludeProductIds() { return excludeProductIds; }
    public void setExcludeProductIds(String excludeProductIds) { this.excludeProductIds = excludeProductIds; }

    public String getAttributes() { return attributes; }
    public void setAttributes(String attributes) { this.attributes = attributes; }
}

