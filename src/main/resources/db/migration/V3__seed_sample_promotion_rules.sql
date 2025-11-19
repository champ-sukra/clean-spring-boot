what-- Migration: V3__seed_sample_promotion_rules.sql
-- Description: Insert sample promotion rules with conditions and actions for testing
-- Version: 3.0
-- Date: 2025-11-16
-- Note: This is test/development data only

-- ============================================================
-- Sample Promotion Rules
-- ============================================================

-- Rule 1: Buy 2 Get 1 Free - Electronics
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, active, priority, quota, quota_used)
SELECT id, 'Buy 2 Get 1 Free - Electronics', '2025-11-01 00:00:00', '2025-12-31 23:59:59', TRUE, 10,
       '{"type":"GLOBAL","limit":1000}', 0
FROM promotion_templates WHERE code = 'TPL_BUY_X_GET_Y';

-- Rule 2: Spend 1000 Get 10% Off
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, active, priority, quota, quota_used)
SELECT id, 'Spend 1000 Get 10% Off', '2025-11-01 00:00:00', '2025-12-31 23:59:59', TRUE, 20,
       '{"type":"PER_CUSTOMER","limit":3}', 0
FROM promotion_templates WHERE code = 'TPL_TOTAL_BILL_DISCOUNT';

-- Rule 3: Fashion Category 20% Off
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, active, priority, quota, quota_used)
SELECT id, 'Fashion Category 20% Off', '2025-11-15 00:00:00', '2025-11-30 23:59:59', TRUE, 30,
       '{"type":"PER_ORDER","limit":1}', 0
FROM promotion_templates WHERE code = 'TPL_CATEGORY_DISCOUNT';

-- Rule 4: Credit Card 5% Cashback
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, active, priority, quota, quota_used)
SELECT id, 'Credit Card 5% Cashback', '2025-11-01 00:00:00', '2025-12-31 23:59:59', TRUE, 15,
       '{"type":"GLOBAL","limit":5000}', 0
FROM promotion_templates WHERE code = 'TPL_PAYMENT_METHOD';

-- Rule 5: First Order 100 Baht Off
INSERT INTO promotion_rules (template_id, rule_name, start_date, end_date, active, priority, quota, quota_used)
SELECT id, 'First Order 100 Baht Off', '2025-11-01 00:00:00', '2025-12-31 23:59:59', TRUE, 5,
       '{"type":"PER_CUSTOMER","limit":1}', 0
FROM promotion_templates WHERE code = 'TPL_FIRST_ORDER';

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

