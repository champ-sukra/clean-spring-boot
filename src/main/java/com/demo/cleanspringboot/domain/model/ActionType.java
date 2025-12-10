package com.demo.cleanspringboot.domain.model;

/**
 * Enum for Promotion Action Types
 * Source: ~/epic.md -- **In-Memory Rule Detail Structure for Evaluation Engine**
 *
 * EvaluateAction type values:
 * DISCOUNT_PERCENT, FIXED_PRICE, FREE_ITEM, FREE_SHIPPING, CASHBACK
 */
public enum ActionType {
    DISCOUNT_PERCENT,
    FIXED_PRICE,
    FREE_ITEM,
    FREE_SHIPPING,
    CASHBACK;

    /**
     * Parse string value to enum, returns null if not valid
     */
    public static ActionType fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return ActionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

