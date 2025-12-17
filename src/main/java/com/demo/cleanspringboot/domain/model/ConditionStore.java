package com.demo.cleanspringboot.domain.model;
import java.util.List;

/**
 * Domain model for EvaluationCondition
 * Source: ~/epic.md -- **In-Memory Rule Detail Structure for Evaluation Engine**
 * Source: ~/sequence-diagram/create-rule-index.puml line 79-80
 *
 * EvaluationCondition fields:
 * - type: ENUM - Condition type (PRODUCT, QUANTITY, AMOUNT, CATEGORY, TOTAL_BILL,
 *                 PAYMENT_METHOD, CUSTOMER_SEGMENT, FIRST_ORDER, CHANNEL, BRAND, SHIPPING_METHOD)
 * - buyQty: INT - Required quantity to qualify (QUANTITY condition only)
 * - minAmount: DECIMAL - Required minimum spend (TOTAL_BILL, AMOUNT condition only)
 * - productCodes: List<String> - List of sorted product-ids (PRODUCT, QUANTITY, AMOUNT conditions)
 * - categoryCodes: List<String> - List of category-ids (CATEGORY, QUANTITY, AMOUNT conditions)
 * - paymentMethods: List<String> - Required payment methods (PAYMENT_METHOD condition)
 * - segment: String - Customer segment (CUSTOMER_SEGMENT condition, e.g., FIRST_ORDER)
 * - shippingMethods: List<String> - Required shipping methods (SHIPPING_METHOD condition)
 * - channels: List<String> - Required channels (CHANNEL condition, e.g., MOBILE, WEB)
 * - brands: List<String> - Required brands (BRAND condition)
 */
public class ConditionStore {
    private ConditionType type;
    private Integer buyQty;
    private Double minAmount;
    private List<String> productCodes;
    private List<String> categoryCodes;
    private List<String> paymentMethods;
    private String segment;
    private List<String> shippingMethods;
    private List<String> channels;
    private List<String> brands;
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
    public List<String> getShippingMethods() {
        return shippingMethods;
    }
    public void setShippingMethods(List<String> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }
    public List<String> getChannels() {
        return channels;
    }
    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
    public List<String> getBrands() {
        return brands;
    }
    public void setBrands(List<String> brands) {
        this.brands = brands;
    }
}
