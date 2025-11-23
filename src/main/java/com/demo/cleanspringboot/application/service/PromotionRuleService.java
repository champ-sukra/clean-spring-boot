package com.demo.cleanspringboot.application.service;
import com.demo.cleanspringboot.application.dto.response.PagedPromotionRuleResponse;
import com.demo.cleanspringboot.application.dto.response.PromotionRuleResponse;
import com.demo.cleanspringboot.application.dto.response.PromotionRuleSummaryResponse;
import com.demo.cleanspringboot.domain.exception.ResourceNotFoundException;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.PromotionRuleRepository;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionRuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional(readOnly = true)
public class PromotionRuleService {
    private final PromotionRuleRepository promotionRuleRepository;
    public PromotionRuleService(PromotionRuleRepository promotionRuleRepository) {
        this.promotionRuleRepository = promotionRuleRepository;
    }
    public PagedPromotionRuleResponse getPromotionRules(Integer status, Long templateId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PromotionRuleEntity> entityPage = promotionRuleRepository.findByFilters(status, templateId, pageable);
        List<PromotionRuleSummaryResponse> items = entityPage.getContent().stream()
            .map(this::transformToPromotionRuleSummaryDTO)
            .collect(Collectors.toList());
        return new PagedPromotionRuleResponse(items, entityPage.getTotalElements(), page, size);
    }
    public PromotionRuleResponse getPromotionRuleDetail(Long ruleId) {
        PromotionRuleEntity entity = promotionRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("PromotionRule", ruleId));
        return transformToPromotionRuleDTO(entity);
    }
    private PromotionRuleResponse transformToPromotionRuleDTO(PromotionRuleEntity e) {
        PromotionRuleResponse r = new PromotionRuleResponse();
        r.setId(e.getId());
        r.setTemplateId(e.getTemplateId());
        r.setRuleName(e.getRuleName());
        r.setStartDate(e.getStartDate());
        r.setEndDate(e.getEndDate());
        r.setStatus(e.getStatus());
        r.setPriority(e.getPriority());
        r.setQuota(e.getQuota());
        r.setQuotaUsed(e.getQuotaUsed());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        r.setConditions(java.util.Collections.emptyList());
        r.setActions(java.util.Collections.emptyList());
        r.setStacking(null);
        return r;
    }
    private PromotionRuleSummaryResponse transformToPromotionRuleSummaryDTO(PromotionRuleEntity e) {
        PromotionRuleSummaryResponse r = new PromotionRuleSummaryResponse();
        r.setId(e.getId());
        r.setTemplateId(e.getTemplateId());
        r.setRuleName(e.getRuleName());
        r.setStartDate(e.getStartDate());
        r.setEndDate(e.getEndDate());
        r.setStatus(e.getStatus());
        r.setPriority(e.getPriority());
        r.setQuota(e.getQuota());
        r.setQuotaUsed(e.getQuotaUsed());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        return r;
    }
}
