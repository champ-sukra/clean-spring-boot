-- Migration: V3__seed_sample_promotion_rules.sql
-- Description: Insert sample promotion rules and test data for evaluation endpoint
-- Version: 3.0
-- Date: 2025-11-16
-- Source: ~/tasks/engine-evaluate.md
-- Note: Includes test/development data and specific test cases for evaluation endpoint

-- ============================================================
-- Clear existing test data (idempotent migration)
-- ============================================================

-- Delete test rules for evaluation endpoint (IDs: 10, 15, 23, 99)
DELETE FROM promotion_stacking WHERE rule_id IN (10, 15, 23, 99);
DELETE FROM promotion_action WHERE rule_id IN (10, 15, 23, 99);
DELETE FROM promotion_condition WHERE rule_id IN (10, 15, 23, 99);
DELETE FROM promotion_rules WHERE id IN (10, 15, 23, 99);

-- Delete sample rules by name (Rules 1-7) using separate statements
SET @rule_id_1 = (SELECT id FROM promotion_rules WHERE rule_name = 'Buy 2 Get 1 Free - Electronics' LIMIT 1);
SET @rule_id_2 = (SELECT id FROM promotion_rules WHERE rule_name = 'Spend 1000 Get 10% Off' LIMIT 1);
SET @rule_id_3 = (SELECT id FROM promotion_rules WHERE rule_name = 'Fashion Category 20% Off' LIMIT 1);
SET @rule_id_4 = (SELECT id FROM promotion_rules WHERE rule_name = 'Credit Card 5% Cashback' LIMIT 1);
SET @rule_id_5 = (SELECT id FROM promotion_rules WHERE rule_name = 'First Order 100 Baht Off' LIMIT 1);
SET @rule_id_6 = (SELECT id FROM promotion_rules WHERE rule_name = 'Spend 2000 Get 15% Off' LIMIT 1);
SET @rule_id_7 = (SELECT id FROM promotion_rules WHERE rule_name = 'Spend 3000 Get Free Item' LIMIT 1);

DELETE FROM promotion_stacking WHERE rule_id IN (@rule_id_1, @rule_id_2, @rule_id_3, @rule_id_4, @rule_id_5, @rule_id_6, @rule_id_7);
DELETE FROM promotion_action WHERE rule_id IN (@rule_id_1, @rule_id_2, @rule_id_3, @rule_id_4, @rule_id_5, @rule_id_6, @rule_id_7);
DELETE FROM promotion_condition WHERE rule_id IN (@rule_id_1, @rule_id_2, @rule_id_3, @rule_id_4, @rule_id_5, @rule_id_6, @rule_id_7);
DELETE FROM promotion_rules WHERE id IN (@rule_id_1, @rule_id_2, @rule_id_3, @rule_id_4, @rule_id_5, @rule_id_6, @rule_id_7);

-- ============================================================
-- Sample Promotion Rules
-- ============================================================

-- Rule 1: Buy 2 Get 1 Free - Electronics
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, status, priority, quota, quota_used)
SELECT id, 'Buy 2 Get 1 Free - Electronics', '2025-11-01 00:00:00', '2025-12-31 23:59:59', 2, 10,
       '{"type":"GLOBAL","limit":1000}', 0
FROM promotion_templates WHERE code = 'TPL_BUY_X_GET_Y';

-- Rule 2: Spend 1000 Get 10% Off
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, status, priority, quota, quota_used)
SELECT id, 'Spend 1000 Get 10% Off', '2025-11-01 00:00:00', '2025-12-31 23:59:59', 2, 20,
       '{"type":"PER_CUSTOMER","limit":3}', 0
FROM promotion_templates WHERE code = 'TPL_TOTAL_BILL_DISCOUNT';

-- Rule 3: Fashion Category 20% Off
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, status, priority, quota, quota_used)
SELECT id, 'Fashion Category 20% Off', '2025-11-15 00:00:00', '2025-11-30 23:59:59', 2, 30,
       '{"type":"PER_ORDER","limit":1}', 0
FROM promotion_templates WHERE code = 'TPL_CATEGORY_DISCOUNT';

-- Rule 4: Credit Card 5% Cashback
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, status, priority, quota, quota_used)
SELECT id, 'Credit Card 5% Cashback', '2025-11-01 00:00:00', '2025-12-31 23:59:59', 2, 15,
       '{"type":"GLOBAL","limit":5000}', 0
FROM promotion_templates WHERE code = 'TPL_PAYMENT_METHOD';

-- Rule 5: First Order 100 Baht Off
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, status, priority, quota, quota_used)
SELECT id, 'First Order 100 Baht Off', '2025-11-01 00:00:00', '2025-12-31 23:59:59', 2, 5,
       '{"type":"PER_CUSTOMER","limit":1}', 0
FROM promotion_templates WHERE code = 'TPL_FIRST_ORDER';

-- Rule 6: Spend 2000 Get 15% Off (Total Bill Discount)
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, status, priority, quota, quota_used)
SELECT id, 'Spend 2000 Get 15% Off', '2025-12-01 00:00:00', '2025-12-31 23:59:59', 2, 25,
       '{"type":"GLOBAL","limit":500}', 0
FROM promotion_templates WHERE code = 'TPL_TOTAL_BILL_DISCOUNT';

-- Rule 7: Spend 3000 Get Free Item (Total Bill Get Free Item)
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, status, priority, quota, quota_used)
SELECT id, 'Spend 3000 Get Free Item', '2025-12-01 00:00:00', '2025-12-31 23:59:59', 2, 28,
       '{"type":"GLOBAL","limit":300}', 0
FROM promotion_templates WHERE code = 'TPL_TOTAL_BILL_GET_FREE_ITEM';

-- ============================================================
-- Sample Promotion Conditions
-- ============================================================

-- Condition for Rule 1: Buy 2 quantity
INSERT INTO promotion_condition (rule_id, condition_type, threshold_value, include_product_ids, attributes)
SELECT id, 'QUANTITY', 2, '["PROD001","PROD002","PROD003"]', '{"buy_qty":2,"get_qty":1,"logic":"OR"}'
FROM promotion_rules WHERE rule_name = 'Buy 2 Get 1 Free - Electronics';

-- Condition for Rule 2: Minimum amount 1000
INSERT INTO promotion_condition (rule_id, condition_type, threshold_value, attributes)
SELECT id, 'AMOUNT', 1000.00, '{"min_amount":1000}'
FROM promotion_rules WHERE rule_name = 'Spend 1000 Get 10% Off';

-- Condition for Rule 3: Fashion category
INSERT INTO promotion_condition (rule_id, condition_type, threshold_value, include_category_ids, attributes)
SELECT id, 'CATEGORY', 0, '["CAT_FASHION","CAT_ACCESSORIES"]', '{"logic":"OR"}'
FROM promotion_rules WHERE rule_name = 'Fashion Category 20% Off';

-- Condition for Rule 4: Credit card payment
INSERT INTO promotion_condition (rule_id, condition_type, threshold_value, attributes)
SELECT id, 'PAYMENT_METHOD', 0, '{"payment_methods":["CREDIT_CARD","DEBIT_CARD"]}'
FROM promotion_rules WHERE rule_name = 'Credit Card 5% Cashback';

-- Condition for Rule 5: First order customer
INSERT INTO promotion_condition (rule_id, condition_type, threshold_value, attributes)
SELECT id, 'CUSTOMER_SEGMENT', 0, '{"segment":"FIRST_ORDER"}'
FROM promotion_rules WHERE rule_name = 'First Order 100 Baht Off';

-- Condition for Rule 6: Total bill minimum 2000
INSERT INTO promotion_condition (rule_id, condition_type, threshold_value, attributes)
SELECT id, 'TOTAL_BILL', 2000.00, '{"min_amount":2000}'
FROM promotion_rules WHERE rule_name = 'Spend 2000 Get 15% Off';

-- Condition for Rule 7: Total bill minimum 3000
INSERT INTO promotion_condition (rule_id, condition_type, threshold_value, attributes)
SELECT id, 'TOTAL_BILL', 3000.00, '{"min_amount":3000}'
FROM promotion_rules WHERE rule_name = 'Spend 3000 Get Free Item';

-- ============================================================
-- Sample Promotion Actions
-- ============================================================

-- Action for Rule 1: Free item
INSERT INTO promotion_action (rule_id, action_type, discount_value, reward_items, attributes)
SELECT id, 'FREE_ITEM', 0, '["PROD001"]', '{"reward_qty":1}'
FROM promotion_rules WHERE rule_name = 'Buy 2 Get 1 Free - Electronics';

-- Action for Rule 2: 10% discount
INSERT INTO promotion_action (rule_id, action_type, discount_value, attributes)
SELECT id, 'DISCOUNT_PERCENT', 10.00, '{"max_discount":500}'
FROM promotion_rules WHERE rule_name = 'Spend 1000 Get 10% Off';

-- Action for Rule 3: 20% discount
INSERT INTO promotion_action (rule_id, action_type, discount_value, attributes)
SELECT id, 'DISCOUNT_PERCENT', 20.00, '{"max_discount":null}'
FROM promotion_rules WHERE rule_name = 'Fashion Category 20% Off';

-- Action for Rule 4: 5% cashback
INSERT INTO promotion_action (rule_id, action_type, discount_value, attributes)
SELECT id, 'CASHBACK', 5.00, '{"cashback_type":"PERCENT","max_cashback":1000}'
FROM promotion_rules WHERE rule_name = 'Credit Card 5% Cashback';

-- Action for Rule 5: Fixed 100 baht discount
INSERT INTO promotion_action (rule_id, action_type, discount_value, attributes)
SELECT id, 'FIXED_PRICE', 100.00, '{"discount_amount":100}'
FROM promotion_rules WHERE rule_name = 'First Order 100 Baht Off';

-- Action for Rule 6: 15% discount
INSERT INTO promotion_action (rule_id, action_type, discount_value, attributes)
SELECT id, 'DISCOUNT_PERCENT', 15.00, '{"max_discount":1000}'
FROM promotion_rules WHERE rule_name = 'Spend 2000 Get 15% Off';

-- Action for Rule 7: Free item
INSERT INTO promotion_action (rule_id, action_type, discount_value, reward_items, attributes)
SELECT id, 'FREE_ITEM', 0, '["GIFT001"]', '{"reward_qty":1}'
FROM promotion_rules WHERE rule_name = 'Spend 3000 Get Free Item';

-- ============================================================
-- Sample Promotion Stacking Rules
-- ============================================================

-- Stacking for Rule 1: Can stack with payment method
INSERT INTO promotion_stacking (rule_id, stackable_with, exclusive_with, combinable)
SELECT id, '["TPL_PAYMENT_METHOD"]', '["TPL_TOTAL_BILL_DISCOUNT"]', TRUE
FROM promotion_rules WHERE rule_name = 'Buy 2 Get 1 Free - Electronics';

-- Stacking for Rule 2: Cannot combine with other discounts
INSERT INTO promotion_stacking (rule_id, stackable_with, exclusive_with, combinable)
SELECT id, '[]', '["TPL_CATEGORY_DISCOUNT","TPL_FIRST_ORDER"]', FALSE
FROM promotion_rules WHERE rule_name = 'Spend 1000 Get 10% Off';

-- Stacking for Rule 3: Can stack with payment method
INSERT INTO promotion_stacking (rule_id, stackable_with, exclusive_with, combinable)
SELECT id, '["TPL_PAYMENT_METHOD"]', '["TPL_TOTAL_BILL_DISCOUNT"]', TRUE
FROM promotion_rules WHERE rule_name = 'Fashion Category 20% Off';

-- Stacking for Rule 4: Can stack with most promotions
INSERT INTO promotion_stacking (rule_id, stackable_with, exclusive_with, combinable)
SELECT id, '["TPL_BUY_X_GET_Y","TPL_CATEGORY_DISCOUNT"]', '[]', TRUE
FROM promotion_rules WHERE rule_name = 'Credit Card 5% Cashback';

-- Stacking for Rule 5: Exclusive (first order only)
INSERT INTO promotion_stacking (rule_id, stackable_with, exclusive_with, combinable)
SELECT id, '[]', '["TPL_TOTAL_BILL_DISCOUNT"]', FALSE
FROM promotion_rules WHERE rule_name = 'First Order 100 Baht Off';

-- Stacking for Rule 6: Can stack with payment method
INSERT INTO promotion_stacking (rule_id, stackable_with, exclusive_with, combinable)
SELECT id, '["TPL_PAYMENT_METHOD"]', '["TPL_BUY_X_GET_Y","TPL_FIRST_ORDER"]', TRUE
FROM promotion_rules WHERE rule_name = 'Spend 2000 Get 15% Off';

-- Stacking for Rule 7: Can stack with payment method
INSERT INTO promotion_stacking (rule_id, stackable_with, exclusive_with, combinable)
SELECT id, '["TPL_PAYMENT_METHOD"]', '["TPL_BUY_X_GET_Y","TPL_FIRST_ORDER","TPL_TOTAL_BILL_DISCOUNT"]', TRUE
FROM promotion_rules WHERE rule_name = 'Spend 3000 Get Free Item';

-- ============================================================
-- Test Data for Evaluation Endpoint (engine-evaluate.md)
-- Test Scenario: QUANTITY conditions for evaluation endpoint
-- Request: cart with SKU-001 qty=2, SKU-002 qty=5
-- Expected Response: eligible_rule_ids=[10, 15, 23]
-- ============================================================

-- Get template ID for Buy X Get Y
SET @template_id = (SELECT id FROM promotion_templates WHERE code = 'TPL_BUY_X_GET_Y' LIMIT 1);

-- Rule 10: Buy 2 SKU-001 Get 10% Off (ELIGIBLE: cart has SKU-001 qty=2)
INSERT INTO promotion_rules (id, template_id, rule_name, start_date, end_date, status, priority, quota, quota_used, created_at, updated_at)
VALUES (
  10, @template_id, 'Buy 2 SKU-001 Get 10% Off',
  DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY),
  2, 1, '{"type":"GLOBAL","limit":1000}', 0, NOW(), NOW()
);

INSERT INTO promotion_condition (id, rule_id, condition_type, threshold_value, include_product_ids, include_category_ids, exclude_product_ids, attributes)
VALUES (10, 10, 'QUANTITY', 2, '["SKU-001"]', NULL, NULL, NULL);

INSERT INTO promotion_action (id, rule_id, action_type, discount_value, reward_items, attributes)
VALUES (10, 10, 'DISCOUNT_PERCENT', 10.00, NULL, '{"reward_qty":1}');

INSERT INTO promotion_stacking (rule_id, stackable_with, exclusive_with, combinable)
VALUES (10, '["TPL_TOTAL_BILL_DISCOUNT"]', '[]', TRUE);

-- Rule 15: Buy 5 SKU-002 Get 15% Off (ELIGIBLE: cart has SKU-002 qty=5)
INSERT INTO promotion_rules (id, template_id, rule_name, start_date, end_date, status, priority, quota, quota_used, created_at, updated_at)
VALUES (
  15, @template_id, 'Buy 5 SKU-002 Get 15% Off',
  DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY),
  2, 2, '{"type":"GLOBAL","limit":500}', 0, NOW(), NOW()
);

INSERT INTO promotion_condition (id, rule_id, condition_type, threshold_value, include_product_ids, include_category_ids, exclude_product_ids, attributes)
VALUES (15, 15, 'QUANTITY', 5, '["SKU-002"]', NULL, NULL, NULL);

INSERT INTO promotion_action (id, rule_id, action_type, discount_value, reward_items, attributes)
VALUES (15, 15, 'DISCOUNT_PERCENT', 15.00, NULL, '{"reward_qty":1}');

INSERT INTO promotion_stacking (rule_id, stackable_with, exclusive_with, combinable)
VALUES (15, '["TPL_TOTAL_BILL_DISCOUNT"]', '[]', TRUE);

-- Rule 23: Buy 3 from Category CAT-100 Get 5% Off (ELIGIBLE: total 7 items from CAT-100)
INSERT INTO promotion_rules (id, template_id, rule_name, start_date, end_date, status, priority, quota, quota_used, created_at, updated_at)
VALUES (
  23, @template_id, 'Buy 3 from Category CAT-100 Get 5% Off',
  DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY),
  2, 3, '{"type":"GLOBAL","limit":2000}', 0, NOW(), NOW()
);

INSERT INTO promotion_condition (id, rule_id, condition_type, threshold_value, include_product_ids, include_category_ids, exclude_product_ids, attributes)
VALUES (23, 23, 'QUANTITY', 3, '["SKU-001","SKU-002"]', '["CAT-100"]', NULL, NULL);

INSERT INTO promotion_action (id, rule_id, action_type, discount_value, reward_items, attributes)
VALUES (23, 23, 'DISCOUNT_PERCENT', 5.00, NULL, '{"reward_qty":1}');

INSERT INTO promotion_stacking (rule_id, stackable_with, exclusive_with, combinable)
VALUES (23, '[]', '[]', TRUE);

-- Rule 99: INACTIVE Test Rule (NOT ELIGIBLE: status=PENDING)
INSERT INTO promotion_rules (id, template_id, rule_name, start_date, end_date, status, priority, quota, quota_used, created_at, updated_at)
VALUES (
  99, @template_id, 'Inactive Test Rule',
  DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY),
  1, 10, '{"type":"GLOBAL","limit":100}', 0, NOW(), NOW()
);

INSERT INTO promotion_condition (id, rule_id, condition_type, threshold_value, include_product_ids, include_category_ids, exclude_product_ids, attributes)
VALUES (99, 99, 'QUANTITY', 1, '["SKU-001"]', NULL, NULL, NULL);

INSERT INTO promotion_action (id, rule_id, action_type, discount_value, reward_items, attributes)
VALUES (99, 99, 'DISCOUNT_PERCENT', 20.00, NULL, NULL);

-- ============================================================
-- Expected Test Results for POST /api/v1/promotion-rules/evaluate
-- Request: {"cart_id": "cart-abc-123", "items": [
--   {"product_id": "SKU-001", "category_id": "CAT-100", "quantity": 2, "price": 100.00},
--   {"product_id": "SKU-002", "category_id": "CAT-100", "quantity": 5, "price": 100.00}
-- ]}
-- Expected Response: {"code": "success", "data": {"eligible_rule_ids": [10, 15, 23]}}
-- ✓ Rule 10: ELIGIBLE (SKU-001 qty=2 >= threshold 2)
-- ✓ Rule 15: ELIGIBLE (SKU-002 qty=5 >= threshold 5)
-- ✓ Rule 23: ELIGIBLE (Total from CAT-100: 2+5=7 >= threshold 3)
-- ✗ Rule 99: NOT ELIGIBLE (status=PENDING, not ACTIVE)
-- ============================================================
