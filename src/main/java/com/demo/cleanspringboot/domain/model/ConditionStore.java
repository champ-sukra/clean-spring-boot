package com.demo.cleanspringboot.domain.model;
import java.util.List;

/**
 * Domain model for EvaluationCondition
 * Source: ~/epic.md -- **In-Memory Rule Detail Structure for Evaluation Engine**
 *
 * EvaluationCondition fields:
 * - type: ENUM - Condition type (QUANTITY, AMOUNT, PRODUCT, CATEGORY, PAYMENT_METHOD,
 *                 CUSTOMER_SEGMENT, FIRST_ORDER, CHANNEL, BRAND, SHIPPING_METHOD)
 * - buyQty: INT - Required quantity to qualify (QUANTITY condition only)
 * - minAmount: DECIMAL - Required minimum spend (AMOUNT condition only)
 * - productCodes: List<String> - Eligible product codes (PRODUCT condition)
 * - categoryCodes: List<String> - Eligible category codes (CATEGORY condition)
 * - paymentMethods: List<String> - Required payment methods
 * - segment: String - Customer segment (e.g., FIRST_ORDER)
 */
public class ConditionStore {
    private ConditionType type;
    private Integer buyQty;
    private Double minAmount;
    private List<String> productCodes;
    private List<String> categoryCodes;
    private List<String> paymentMethods;
    private String segment;
    public ConditionStore() {
    }
    public ConditionType getType() {
        return type;
    }
    public void setType(ConditionType type) {
        this.type = type;
    }
    public Integer getBuyQty() {
        return buyQty;
    }
    public void setBuyQty(Integer buyQty) {
        this.buyQty = buyQty;
    }
    public Double getMinAmount() {
        return minAmount;
    }
    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }
    public List<String> getProductCodes() {
        return productCodes;
    }
    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }
    public List<String> getCategoryCodes() {
        return categoryCodes;
    }
    public void setCategoryCodes(List<String> categoryCodes) {
        this.categoryCodes = categoryCodes;
    }
    public List<String> getPaymentMethods() {
        return paymentMethods;
    }
    public void setPaymentMethods(List<String> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
    public String getSegment() {
        return segment;
    }
    public void setSegment(String segment) {
        this.segment = segment;
    }
}
