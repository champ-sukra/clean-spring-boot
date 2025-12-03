package com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

/**
 * JPA Entity for promotion_action table
 */
@Entity
@Table(name = "promotion_action")
public class PromotionActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_id", nullable = false)
    private Long ruleId;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "discount_value")
    private String discountValue;

    @Column(name = "reward_items", columnDefinition = "JSON")
    private String rewardItems;

    @Column(name = "attributes", columnDefinition = "JSON")
    private String attributes;

    public PromotionActionEntity() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getDiscountValue() { return discountValue; }
    public void setDiscountValue(String discountValue) { this.discountValue = discountValue; }

    public String getRewardItems() { return rewardItems; }
    public void setRewardItems(String rewardItems) { this.rewardItems = rewardItems; }

    public String getAttributes() { return attributes; }
    public void setAttributes(String attributes) { this.attributes = attributes; }
}

