package com.demo.cleanspringboot.application.dto.response;
import java.util.List;
public class PagedPromotionRuleResponse {
    private List<PromotionRuleSummaryResponse> items;
    private Long total;
    private Integer page;
    private Integer size;
    public PagedPromotionRuleResponse() {}
    public PagedPromotionRuleResponse(List<PromotionRuleSummaryResponse> items, Long total, Integer page, Integer size) {
        this.items = items;
        this.total = total;
        this.page = page;
        this.size = size;
    }
    public List<PromotionRuleSummaryResponse> getItems() { return items; }
    public void setItems(List<PromotionRuleSummaryResponse> items) { this.items = items; }
    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
