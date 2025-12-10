package com.demo.cleanspringboot.domain.model;

import java.util.List;
import java.util.Map;

/**
 * Domain model for EvaluateAction
 * Source: ~/epic.md -- **In-Memory Rule Detail Structure for Evaluation Engine**
 *
 * EvaluateAction fields:
 * - type: ENUM - Action type (DISCOUNT_PERCENT, FIXED_PRICE, FREE_ITEM, FREE_SHIPPING, CASHBACK)
 * - discountPercent: DECIMAL - Discount percentage (0–100)
 * - fixedPrice: DECIMAL - Fixed price for rewarded item
 * - rewardProductCodes: List<String> - Reward product codes (for PWP / Buy X Get Y)
 * - rewardQty: INT - Number of items rewarded
 * - attributes: JSON Map - Additional optional parameters
 */
public class ActionStore {

    private ActionType type;
    private Double discountPercent;
    private Double fixedPrice;
    private List<String> rewardProductCodes;
    private Integer rewardQty;
    private Map<String, Object> attributes;

    public ActionStore() {
    }

    // Getters and Setters
    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Double getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(Double fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public List<String> getRewardProductCodes() {
        return rewardProductCodes;
    }

    public void setRewardProductCodes(List<String> rewardProductCodes) {
        this.rewardProductCodes = rewardProductCodes;
    }

    public Integer getRewardQty() {
        return rewardQty;
    }

    public void setRewardQty(Integer rewardQty) {
        this.rewardQty = rewardQty;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}

