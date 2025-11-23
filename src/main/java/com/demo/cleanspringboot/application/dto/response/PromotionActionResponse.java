package com.demo.cleanspringboot.application.dto.response;
import java.util.List;
public class PromotionActionResponse {
    private Long id;
    private Long ruleId;
    private String actionType;
    private Double discountValue;
    private List<String> rewardItems;
    private Object attributes;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public Double getDiscountValue() { return discountValue; }
    public void setDiscountValue(Double discountValue) { this.discountValue = discountValue; }
    public List<String> getRewardItems() { return rewardItems; }
    public void setRewardItems(List<String> rewardItems) { this.rewardItems = rewardItems; }
    public Object getAttributes() { return attributes; }
    public void setAttributes(Object attributes) { this.attributes = attributes; }
}
