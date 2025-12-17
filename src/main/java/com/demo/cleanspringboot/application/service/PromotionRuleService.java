package com.demo.cleanspringboot.application.service;
import com.demo.cleanspringboot.application.dto.response.PagedPromotionRuleResponse;
import com.demo.cleanspringboot.application.dto.response.PromotionRuleResponse;
import com.demo.cleanspringboot.application.dto.response.PromotionRuleSummaryResponse;
import com.demo.cleanspringboot.domain.exception.ResourceNotFoundException;
import com.demo.cleanspringboot.domain.model.EvaluatePromotionRule;
import com.demo.cleanspringboot.domain.model.RuleIndex;
import com.demo.cleanspringboot.domain.service.PromotionRuleIndexDomainService;
import com.demo.cleanspringboot.domain.service.PromotionRuleDomainService;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionActionEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionConditionEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.entity.PromotionRuleEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.PromotionActionRepository;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.PromotionConditionRepository;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.PromotionRuleRepository;
import com.demo.cleanspringboot.infrastructure.cache.RuleCache;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Application Service for PromotionRule
 * As shown in sequence diagram: (application/service)
 */
@Service
@Transactional(readOnly = true)
public class PromotionRuleService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionRuleService.class);

    private final PromotionRuleRepository promotionRuleRepository;
    private final PromotionConditionRepository promotionConditionRepository;
    private final PromotionActionRepository promotionActionRepository;
    private final PromotionRuleIndexDomainService promotionRuleIndexDomainService;
    private final PromotionRuleDomainService promotionRuleDomainService;
    private final RuleCache ruleCache;

    public PromotionRuleService(
            PromotionRuleRepository promotionRuleRepository,
            PromotionConditionRepository promotionConditionRepository,
            PromotionActionRepository promotionActionRepository,
            PromotionRuleIndexDomainService promotionRuleIndexDomainService,
            PromotionRuleDomainService promotionRuleDomainService,
            RuleCache ruleCache) {
        this.promotionRuleRepository = promotionRuleRepository;
        this.promotionConditionRepository = promotionConditionRepository;
        this.promotionActionRepository = promotionActionRepository;
        this.promotionRuleIndexDomainService = promotionRuleIndexDomainService;
        this.promotionRuleDomainService = promotionRuleDomainService;
        this.ruleCache = ruleCache;
    }

    /**
     * Initialize cache on application startup to prevent slow first request
     * Warms up the rule index, evaluate rules cache, and Jackson serializers
     */
    @PostConstruct
    public void initializeCache() {
        logger.info("Initializing promotion rule cache on application startup...");
        try {
            // Build rule index and cache
            buildPromotionRuleData();
            logger.info("Promotion rule cache initialized successfully");

            // Warm up Jackson serializers/deserializers to avoid first-request penalty
            warmupJackson();
            logger.info("Jackson serializers warmed up successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize promotion rule cache", e);
            // Don't fail application startup, cache will be built on first request
        }
    }

    /**
     * Warm up Jackson ObjectMapper by serializing/deserializing sample objects
     * This eliminates the 70-80ms overhead on the first HTTP request
     */
    private void warmupJackson() {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.findAndRegisterModules(); // Register all Jackson modules

            // Warm up with a realistic large request (15 items like production)
            String sampleRequest = """
                {
                  "cart_id": "warmup",
                  "customer_id": "12345",
                  "items": [
                    {"product_id": "SKU-001", "category_id": "CAT-100", "quantity": 1, "price": 100.00},
                    {"product_id": "SKU-002", "category_id": "CAT-100", "quantity": 2, "price": 200.00},
                    {"product_id": "SKU-003", "category_id": "CAT-100", "quantity": 3, "price": 300.00},
                    {"product_id": "SKU-004", "category_id": "CAT-100", "quantity": 4, "price": 400.00},
                    {"product_id": "SKU-005", "category_id": "CAT-100", "quantity": 5, "price": 500.00},
                    {"product_id": "SKU-006", "category_id": "CAT-100", "quantity": 6, "price": 600.00},
                    {"product_id": "SKU-007", "category_id": "CAT-100", "quantity": 7, "price": 700.00},
                    {"product_id": "SKU-008", "category_id": "CAT-100", "quantity": 8, "price": 800.00},
                    {"product_id": "SKU-009", "category_id": "CAT-100", "quantity": 9, "price": 900.00},
                    {"product_id": "SKU-010", "category_id": "CAT-100", "quantity": 10, "price": 1000.00},
                    {"product_id": "SKU-011", "category_id": "CAT-100", "quantity": 11, "price": 1100.00},
                    {"product_id": "SKU-012", "category_id": "CAT-100", "quantity": 12, "price": 1200.00},
                    {"product_id": "SKU-013", "category_id": "CAT-100", "quantity": 13, "price": 1300.00},
                    {"product_id": "SKU-014", "category_id": "CAT-100", "quantity": 14, "price": 1400.00},
                    {"product_id": "SKU-015", "category_id": "CAT-100", "quantity": 15, "price": 1500.00}
                  ],
                  "payment_method": "CREDIT_CARD",
                  "shipping_method": "EXPRESS"
                }
                """;

            // Deserialize multiple times to warm up JIT
            for (int i = 0; i < 3; i++) {
                com.demo.cleanspringboot.application.dto.request.EvaluatePromotionRequest req =
                    mapper.readValue(sampleRequest, com.demo.cleanspringboot.application.dto.request.EvaluatePromotionRequest.class);
            }

            // Warm up EvaluatePromotionResponse serialization
            com.demo.cleanspringboot.application.dto.response.EvaluatePromotionResponse resp =
                new com.demo.cleanspringboot.application.dto.response.EvaluatePromotionResponse();
            resp.getData().setEligibleRuleIds(java.util.List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L));

            // Serialize multiple times to warm up JIT
            for (int i = 0; i < 3; i++) {
                mapper.writeValueAsString(resp);
            }

            logger.debug("Jackson warmup completed with 3 iterations");
        } catch (Exception e) {
            logger.warn("Jackson warmup failed, first request may be slower", e);
        }
    }

    /**
     * Build promotion rule data (rule index + evaluate rules)
     * As shown in sequence diagram: buildPromotionRuleData()
     */
    public void buildPromotionRuleData() {
        logger.info("Starting to build promotion rule data");

        // Step 1: Find active promotion rules from repository
        List<PromotionRuleEntity> activeRules = promotionRuleRepository.findActivePromotionRules();
        logger.info("Found {} active promotion rules", activeRules.size());

        if (activeRules.isEmpty()) {
            logger.warn("No active rules found, clearing cache");
            ruleCache.clear();
            return;
        }

        // Step 2: Load conditions and actions for all active rules
        Map<Long, List<PromotionConditionEntity>> conditionsMap = activeRules.stream()
                .collect(Collectors.toMap(
                        PromotionRuleEntity::getId,
                        rule -> promotionConditionRepository.findByRuleId(rule.getId())
                ));

        Map<Long, List<PromotionActionEntity>> actionsMap = activeRules.stream()
                .collect(Collectors.toMap(
                        PromotionRuleEntity::getId,
                        rule -> promotionActionRepository.findByRuleId(rule.getId())
                ));

        // Step 3: Generate rule index using PromotionRuleIndexDomainService
        RuleIndex ruleIndex = promotionRuleIndexDomainService.generateRuleIndex(activeRules, conditionsMap);

        // Step 4: Save rule index to cache
        ruleCache.saveRuleIndex(ruleIndex);
        logger.info("Rule index saved to cache");

        // Step 5: Build evaluate rules using PromotionRuleDomainService (sequence diagram line 72)
        Map<Long, EvaluatePromotionRule> evaluateRules =
                promotionRuleDomainService.buildEvaluateRules(activeRules, conditionsMap, actionsMap);

        // Step 6: Save evaluate rules to cache (sequence diagram line 85)
        ruleCache.saveEvaluateRules(evaluateRules);
        logger.info("Evaluate promotion rules saved to cache");

        logger.info("Promotion rule data build completed successfully");
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
