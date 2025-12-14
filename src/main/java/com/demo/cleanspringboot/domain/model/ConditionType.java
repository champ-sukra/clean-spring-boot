package com.demo.cleanspringboot.domain.model;

/**
 * Enum for Promotion Condition Types
 * Source: ~/epic.md -- **In-Memory Rule Detail Structure for Evaluation Engine**
 * Source: ~/sequence-diagram/evaluate-promotion.puml line 63-65
 * Source: ~/sequence-diagram/create-rule-index.puml line 80
 *
 * EvaluationCondition type values (from epic.md):
 * QUANTITY, AMOUNT, CATEGORY, PAYMENT_METHOD, CUSTOMER_SEGMENT,
 * FIRST_ORDER, CHANNEL, BRAND, SHIPPING_METHOD, TOTAL_BILL
 *
 * Additional: PRODUCT (for product-specific conditions)
 */
public enum ConditionType {
    QUANTITY,
    AMOUNT,
    TOTAL_BILL,
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

