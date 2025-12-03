package com.demo.cleanspringboot.domain.model;

/**
 * Domain model for Quota
 */
public class Quota {

    private Integer totalQuota;
    private Integer usedQuota;
    private Integer remainingQuota;

    public Quota() {
    }

    public Quota(Integer totalQuota, Integer usedQuota) {
        this.totalQuota = totalQuota;
        this.usedQuota = usedQuota;
        this.remainingQuota = totalQuota != null && usedQuota != null
            ? totalQuota - usedQuota
            : null;
    }

    /**
     * Constructor that accepts String quota (JSON format) and Integer usedQuota
     * For cases where quota is stored as JSON string in database
     */
    public Quota(String quotaJson, Integer usedQuota) {
        // For now, we'll parse simple integer from JSON string
        // If quota is stored as JSON object, this needs to be enhanced
        try {
            if (quotaJson != null && !quotaJson.isEmpty()) {
                // Try to parse as integer (if it's just a number)
                this.totalQuota = Integer.parseInt(quotaJson.replaceAll("[^0-9]", ""));
            }
        } catch (Exception e) {
            this.totalQuota = null;
        }
        this.usedQuota = usedQuota;
        updateRemainingQuota();
    }

    // Getters and Setters
    public Integer getTotalQuota() {
        return totalQuota;
    }

    public void setTotalQuota(Integer totalQuota) {
        this.totalQuota = totalQuota;
        updateRemainingQuota();
    }

    public Integer getUsedQuota() {
        return usedQuota;
    }

    public void setUsedQuota(Integer usedQuota) {
        this.usedQuota = usedQuota;
        updateRemainingQuota();
    }

    public Integer getRemainingQuota() {
        return remainingQuota;
    }

    private void updateRemainingQuota() {
        if (totalQuota != null && usedQuota != null) {
            this.remainingQuota = totalQuota - usedQuota;
        }
    }
}

