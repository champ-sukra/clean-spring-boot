package com.demo.cleanspringboot.infrastructure.adapter.in.web;

import com.demo.cleanspringboot.application.dto.response.PromotionRuleResponse;
import com.demo.cleanspringboot.application.service.PromotionRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for PromotionRule operations
 * Inbound adapter - translates HTTP to service calls
 */
@RestController
@RequestMapping("/api/v1/promotion-rules")
public class PromotionRuleController {

    private final PromotionRuleService promotionRuleService;

    public PromotionRuleController(PromotionRuleService promotionRuleService) {
        this.promotionRuleService = promotionRuleService;
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

