-- Migration: V2__seed_promotion_templates.sql
-- Description: Insert sample promotion templates for development/testing
-- Version: 2.0
-- Date: 2025-11-16

-- ============================================================
-- Insert Sample Promotion Templates (Idempotent)
-- ============================================================

INSERT INTO promotion_templates (code, name, description, active) VALUES
('TPL_BUY_X_GET_Y',
 'Buy X Get Y',
 'Customer buys X items and gets Y items free or discounted. Common for bundle promotions.',
 TRUE),

('TPL_TOTAL_BILL_DISCOUNT',
 'Total Bill Discount',
 'Discount based on total bill amount. Encourages higher cart value.',
 TRUE),

('TPL_CATEGORY_DISCOUNT',
 'Category Discount',
 'Discount on specific product categories. Used for category-specific campaigns.',
 TRUE),

('TPL_PAYMENT_METHOD',
 'Payment Method Promotion',
 'Special discount for specific payment methods (credit card, mobile wallet, etc.).',
 TRUE),

('TPL_FIRST_ORDER',
 'First Order Promotion',
 'Special offer for first-time customers. Welcome campaign.',
 TRUE),

('TPL_FREE_SHIPPING',
 'Free Shipping',
 'Waive shipping fee based on order conditions.',
 TRUE),

('TPL_BUNDLE_DEAL',
 'Bundle Deal',
 'Special price when buying specific product combinations.',
 TRUE),

('TPL_LOYALTY_REWARD',
 'Loyalty Reward',
 'Rewards for loyal customers based on customer segment.',
 TRUE),

('TPL_TOTAL_BILL_GET_FREE_ITEM',
 'Total Bill Get Free Item',
 'Get free item when total bill meets minimum amount threshold.',
 TRUE)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  description = VALUES(description),
  active = VALUES(active),
  updated_at = CURRENT_TIMESTAMP;

