package com.demo.cleanspringboot.domain.model;

import java.util.List;

/**
 * Stacking configuration for promotion rules
 * Defines which rules can be combined together
 * Source: ~/epic.md -- promotion_stacking table
 */
public class StackingConfig {

    private List<String> stackableWith;  // List of template codes or rule IDs allowed to combine
    private List<String> exclusiveWith;  // List of template codes or rule IDs not allowed to combine
    private Boolean combinable;          // Whether stacking is permitted

    public StackingConfig() {
    }

    public StackingConfig(List<String> stackableWith, List<String> exclusiveWith, Boolean combinable) {
        this.stackableWith = stackableWith;
        this.exclusiveWith = exclusiveWith;
        this.combinable = combinable;
    }

    // Getters and Setters
    public List<String> getStackableWith() {
        return stackableWith;
    }

    public void setStackableWith(List<String> stackableWith) {
        this.stackableWith = stackableWith;
    }

    public List<String> getExclusiveWith() {
        return exclusiveWith;
    }

    public void setExclusiveWith(List<String> exclusiveWith) {
        this.exclusiveWith = exclusiveWith;
    }

    public Boolean getCombinable() {
        return combinable;
    }

    public void setCombinable(Boolean combinable) {
        this.combinable = combinable;
    }
}

