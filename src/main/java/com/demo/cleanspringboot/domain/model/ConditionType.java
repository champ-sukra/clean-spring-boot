package com.demo.cleanspringboot.domain.model;

/**
 * Enum for Promotion Condition Types
 * Source: ~/epic.md -- **In-Memory Rule Detail Structure for Evaluation Engine**
 *
 * EvaluationCondition type values:
 * QUANTITY, AMOUNT, PRODUCT, CATEGORY, PAYMENT_METHOD,
 * CUSTOMER_SEGMENT, FIRST_ORDER, CHANNEL, BRAND, SHIPPING_METHOD
 */
public enum ConditionType {
    QUANTITY,
    AMOUNT,
    PRODUCT,
    CATEGORY,
    PAYMENT_METHOD,
    CUSTOMER_SEGMENT,
    FIRST_ORDER,
    CHANNEL,
    BRAND,
    SHIPPING_METHOD;

    /**
     * Parse string value to enum, returns null if not valid
     */
    public static ConditionType fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return ConditionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

