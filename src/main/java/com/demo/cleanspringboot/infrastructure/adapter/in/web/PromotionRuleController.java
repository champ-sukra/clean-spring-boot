package com.demo.cleanspringboot.infrastructure.adapter.in.web;

import com.demo.cleanspringboot.application.dto.response.PagedPromotionRuleResponse;
import com.demo.cleanspringboot.application.dto.response.PromotionRuleResponse;
import com.demo.cleanspringboot.application.service.PromotionRuleService;
import com.demo.cleanspringboot.common.exception.ErrorResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for PromotionRule operations
 * Inbound adapter - translates HTTP to service calls
 */
@RestController
@RequestMapping("/api/v1/promotion-rules")
public class PromotionRuleController {

    private static final Logger logger = LoggerFactory.getLogger(PromotionRuleController.class);

    private final PromotionRuleService promotionRuleService;

    public PromotionRuleController(PromotionRuleService promotionRuleService) {
        this.promotionRuleService = promotionRuleService;
    }

    /**
     * Initialize and build rule index on application startup
     * As shown in sequence diagram: preparePromotionRuleData()
     */
    @PostConstruct
    public void preparePromotionRuleData() {
        logger.info("Application startup - Preparing promotion rule data");
        try {
            promotionRuleService.buildPromotionRuleData();
            logger.info("Promotion rule data initialization completed successfully");
        } catch (Exception e) {
            logger.error("Failed to prepare promotion rule data on startup", e);
            // Don't fail the application startup, but log the error
        }
    }

    /**
     * Manual endpoint to rebuild rule index
     * Can be called by scheduler or admin
     */
    @PostMapping("/rebuild-index")
    public ResponseEntity<ApiResponse<String>> rebuildRuleIndex() {
        logger.info("Manual rule index rebuild requested");
        try {
            promotionRuleService.buildPromotionRuleData();
            return ResponseEntity.ok(new ApiResponse<>("success", "Rule index rebuilt successfully"));
        } catch (Exception e) {
            logger.error("Failed to rebuild rule index", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("error", "Failed to rebuild rule index: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getPromotionRules(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long templateId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {

        // Validate query params
        if (status != null && (status < 1 || status > 3)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("invalid_request", "Invalid status value"));
        }
        if (page != null && page < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("invalid_request", "Invalid page value"));
        }
        if (size != null && (size < 1 || size > 100)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("invalid_request", "Invalid size value"));
        }

        PagedPromotionRuleResponse pagedResponse = promotionRuleService.getPromotionRules(
            status, templateId, page, size);

        return ResponseEntity.ok(new ApiResponse<>("success", pagedResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PromotionRuleResponse>> getPromotionRuleDetail(
            @PathVariable Long id) {

        PromotionRuleResponse response = promotionRuleService.getPromotionRuleDetail(id);

        return ResponseEntity.ok(
            new ApiResponse<>("success", response)
        );
    }

    /**
     * Standard API response envelope
     */
    public static class ApiResponse<T> {
        private String code;
        private T data;

        public ApiResponse(String code, T data) {
            this.code = code;
            this.data = data;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
    }
}

