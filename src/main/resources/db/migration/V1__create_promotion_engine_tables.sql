-- Migration: V1__create_promotion_engine_tables.sql
-- Description: Create all promotion engine tables
-- Version: 1.0
-- Date: 2025-11-16
-- Database: MySQL 8.0+

-- ============================================================
-- Step 1: Create promotion_templates table (no dependencies)
-- ============================================================
CREATE TABLE IF NOT EXISTS promotion_templates (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) UNIQUE NOT NULL COMMENT 'Unique logic code (e.g., TPL_BUY_X_GET_Y)',
  name VARCHAR(255) NOT NULL COMMENT 'Human-readable template name',
  description TEXT COMMENT 'Template description and business intent',
  active BOOLEAN DEFAULT TRUE COMMENT 'Whether template is active',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_code (code),
  INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Reusable promotion template definitions';

-- ============================================================
-- Step 2: Create promotion_rules table (depends on templates)
-- ============================================================
CREATE TABLE IF NOT EXISTS promotion_rules (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  template_id INT UNSIGNED NOT NULL COMMENT 'References promotion template',
  rule_name VARCHAR(255) NOT NULL COMMENT 'Business name of the rule',
  start_date DATETIME NOT NULL COMMENT 'Rule effective start date',
  end_date DATETIME NOT NULL COMMENT 'Rule expiration date',
  status INT DEFAULT 1 COMMENT 'Rule status: 1=PENDING, 2=ACTIVE, 3=EXPIRED',
  priority INT DEFAULT 0 COMMENT 'Stacking order (lower = higher priority)',
  quota JSON COMMENT 'Quota configuration: {"type":"GLOBAL|PER_CUSTOMER|PER_ORDER|PER_PRODUCT","limit":number}',
  quota_used INT DEFAULT 0 COMMENT 'Current redemption count',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (template_id) REFERENCES promotion_templates(id) ON DELETE RESTRICT,
  INDEX idx_template_id (template_id),
  INDEX idx_status (status),
  INDEX idx_dates (start_date, end_date),
  INDEX idx_priority (priority),
  CHECK (end_date > start_date),
  CHECK (quota_used >= 0),
  CHECK (status IN (1, 2, 3))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Active promotion rule instances';

-- ============================================================
-- Step 3: Create promotion_condition table (depends on rules)
-- ============================================================
CREATE TABLE IF NOT EXISTS promotion_condition (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  rule_id INT UNSIGNED NOT NULL COMMENT 'References promotion rule',
  condition_type ENUM('QUANTITY','AMOUNT','CATEGORY','CUSTOMER_SEGMENT','CHANNEL','PAYMENT_METHOD') NOT NULL COMMENT 'Type of eligibility condition',
  threshold_value DECIMAL(10,2) DEFAULT 0 COMMENT 'Minimum quantity or amount required',
  include_product_ids JSON COMMENT 'SKUs or product groups that qualify',
  include_category_ids JSON COMMENT 'Category groups that qualify',
  exclude_product_ids JSON COMMENT 'SKUs or groups to exclude',
  attributes JSON COMMENT 'Flexible metadata (e.g., {"buy_qty":2,"logic":"AND"})',
  FOREIGN KEY (rule_id) REFERENCES promotion_rules(id) ON DELETE CASCADE,
  INDEX idx_rule_id (rule_id),
  INDEX idx_condition_type (condition_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Eligibility conditions for promotion rules';

-- ============================================================
-- Step 4: Create promotion_action table (depends on rules)
-- ============================================================
CREATE TABLE IF NOT EXISTS promotion_action (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  rule_id INT UNSIGNED NOT NULL COMMENT 'References promotion rule',
  action_type ENUM('DISCOUNT_PERCENT','FIXED_PRICE','FREE_ITEM','FREE_SHIPPING','CASHBACK') NOT NULL COMMENT 'Type of reward action',
  discount_value DECIMAL(10,2) DEFAULT 0 COMMENT 'Discount amount or percentage',
  reward_items JSON COMMENT 'Reward SKUs or item list for FREE_ITEM',
  attributes JSON COMMENT 'Extra parameters (e.g., {"reward_qty":1,"fixed_price":99})',
  FOREIGN KEY (rule_id) REFERENCES promotion_rules(id) ON DELETE CASCADE,
  INDEX idx_rule_id (rule_id),
  INDEX idx_action_type (action_type),
  CHECK (discount_value >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Reward actions for promotion rules';

-- ============================================================
-- Step 5: Create promotion_stacking table (depends on rules)
-- ============================================================
CREATE TABLE IF NOT EXISTS promotion_stacking (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  rule_id INT UNSIGNED NOT NULL UNIQUE COMMENT 'References promotion rule (1:1)',
  stackable_with JSON COMMENT 'Template codes or rule IDs allowed to combine',
  exclusive_with JSON COMMENT 'Templates or rules not allowed to combine',
  combinable BOOLEAN DEFAULT TRUE COMMENT 'Whether stacking is permitted',
  FOREIGN KEY (rule_id) REFERENCES promotion_rules(id) ON DELETE CASCADE,
  INDEX idx_rule_id (rule_id),
  INDEX idx_combinable (combinable)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Stacking configuration per rule';

-- ============================================================
-- Step 6: Create promotion_result_log table (depends on rules)
-- ============================================================
CREATE TABLE IF NOT EXISTS promotion_result_log (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  rule_id INT UNSIGNED NOT NULL COMMENT 'Which rule produced this result',
  basket_id VARCHAR(64) NOT NULL COMMENT 'Cart or session identifier',
  status ENUM('APPLIED','NOT_APPLIED','PARTIAL','INACTIVE') NOT NULL COMMENT 'Evaluation result status',
  result JSON COMMENT 'Serialized promotion result (discounts, messages, etc.)',
  evaluated_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Evaluation timestamp',
  FOREIGN KEY (rule_id) REFERENCES promotion_rules(id) ON DELETE CASCADE,
  INDEX idx_rule_id (rule_id),
  INDEX idx_basket_id (basket_id),
  INDEX idx_evaluated_at (evaluated_at),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Promotion evaluation history and analytics';

