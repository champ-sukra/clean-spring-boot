package com.demo.cleanspringboot.application.port.out;

import com.demo.cleanspringboot.domain.model.PromotionRule;
import java.util.Optional;

/**
 * Outbound port for PromotionRule persistence
 * Defines the contract for repository operations needed by application layer
 */
public interface PromotionRuleRepositoryPort {

    Optional<PromotionRule> findById(Long id);

    PromotionRule save(PromotionRule promotionRule);

    void deleteById(Long id);
}

