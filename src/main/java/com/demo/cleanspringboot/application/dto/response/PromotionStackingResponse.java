package com.demo.cleanspringboot.application.dto.response;
import java.util.List;
public class PromotionStackingResponse {
    private Long id;
    private Long ruleId;
    private List<String> stackableWith;
    private List<String> exclusiveWith;
    private Boolean combinable;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public List<String> getStackableWith() { return stackableWith; }
    public void setStackableWith(List<String> stackableWith) { this.stackableWith = stackableWith; }
    public List<String> getExclusiveWith() { return exclusiveWith; }
    public void setExclusiveWith(List<String> exclusiveWith) { this.exclusiveWith = exclusiveWith; }
    public Boolean getCombinable() { return combinable; }
    public void setCombinable(Boolean combinable) { this.combinable = combinable; }
}
