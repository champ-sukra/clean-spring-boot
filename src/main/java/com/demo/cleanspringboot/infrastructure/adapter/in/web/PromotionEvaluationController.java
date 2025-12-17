package com.demo.cleanspringboot.infrastructure.adapter.in.web;

import com.demo.cleanspringboot.application.dto.request.EvaluatePromotionRequest;
import com.demo.cleanspringboot.application.dto.response.EvaluatePromotionResponse;
import com.demo.cleanspringboot.application.service.PromotionEvaluationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for promotion evaluation
 * Source: ~/sequence-diagram/evaluate-promotion.puml
 * Participant: PromotionEvaluationController\n(infrastructure/adapter/in/web)
 *
 * Endpoint: POST /api/v1/promotion-rules/evaluate
 */
@RestController
@RequestMapping("/api/v1/promotion-rules")
public class PromotionEvaluationController {

    private static final Logger logger = LoggerFactory.getLogger(PromotionEvaluationController.class);

    private final PromotionEvaluationService promotionEvaluationService;

    public PromotionEvaluationController(PromotionEvaluationService promotionEvaluationService) {
        this.promotionEvaluationService = promotionEvaluationService;
    }

    /**
     * Evaluate eligible promotions for cart
     * Source: ~/tasks/engine-evaluate.md
     * POST /api/v1/promotion-rules/evaluate
     */
    @PostMapping("/evaluate")
    public ResponseEntity<EvaluatePromotionResponse> evaluatePromotions(
            @RequestBody EvaluatePromotionRequest request) {

        long controllerStart = System.nanoTime();
        logger.info("Received promotion evaluation request for cartId: {}", request.getCartId());

        // Call service to evaluate promotion rules (line 33)
        long serviceStart = System.nanoTime();
        List<Long> eligibleRuleIds = promotionEvaluationService.evaluatePromotionRules(request);
        long serviceTime = (System.nanoTime() - serviceStart) / 1_000_000;

        // Transform to response (line 61-62)
        long responseStart = System.nanoTime();
        EvaluatePromotionResponse response = new EvaluatePromotionResponse();
        response.getData().setEligibleRuleIds(eligibleRuleIds);
        long responseTime = (System.nanoTime() - responseStart) / 1_000_000;

        long controllerTime = (System.nanoTime() - controllerStart) / 1_000_000;
        logger.info("Controller completed in {}ms (service: {}ms, response: {}ms). Returning {} eligible rules for cartId: {}",
                    controllerTime, serviceTime, responseTime, eligibleRuleIds.size(), request.getCartId());

        // Return 200 OK (line 64)
        return ResponseEntity.ok(response);
    }
}

