# Promotion-Engine Capabilities EPIC

**Page ID:** 640155657  
**Last Updated:** 2025-11-30T01:08:38.788Z  
**Version:** 9

## Objective
To build a promotion engine that support multiple stackable, configurable promotion templates.

## Business Capabilities

1. **Template Management** – manage promotion templates including Buy X Get Y, Total bill Get Discount
2. **Rule** – generate rules from templates with start / end dates, stacking, quota and priorities
3. **Condition** – first order, payment method, SKUs to be allow
4. **Action** – rewards to be given including discount %, fixed price, free item
5. **Stacking control** – determine whether a promotion rule can combine with others

## Domain Driven Design

**Domain:** Promotion  
**SubDomain:** Campaign, Rule-Engine, Redemption

| SubDomain | Function | Note |
|-----------|----------|------|
| Campaign | - able to create campaign<br>- able to update campaign<br>- able to set start and stop | Campaign → BUY X GET Y, TOTAL BILL GET X, .. |
| Rule-Engine | - able to create rule<br>- able to assign conditions to rule<br>- able to assign actions to rule | - Condition must support both 'and' or 'or' |
| Redemption | - able to evaluate<br>- able to redeem<br>- able to cancel redeem | |

## Technology Stack

- **Database:** RDS - MySQL
- **Programming language:** Golang
- **Caching:** In-memory

## Database Schema

### Table: `promotion_templates`

| Field | Type | Description |
|-------|------|-------------|
| `id` | INT UNSIGNED (PK) | Unique identifier of the promotion template |
| `code` | VARCHAR(64) | Unique logic code (e.g., `TPL_BUY_X_GET_Y`, `TPL_TOTAL_BILL_DISCOUNT`) used by the engine to select logic |
| `name` | VARCHAR(255) | Human-readable template name (e.g., "Buy X Get Y") |
| `description` | TEXT | Describes what the template does and its business intent |
| `active` | BOOLEAN | Whether the template is currently active |
| `created_at` | DATETIME | Record creation timestamp |
| `updated_at` | DATETIME | Last update timestamp |

### Table: `promotion_rules`

| Field | Type | Description |
|-------|------|-------------|
| `id` | INT UNSIGNED (PK) | Unique identifier of the rule |
| `template_id` | INT UNSIGNED (FK → promotion_template.id) | References the template this rule belongs to |
| `rule_name` | VARCHAR(255) | Business name of the promotion rule |
| `start_date` | DATETIME | Rule effective start date |
| `end_date` | DATETIME | Rule expiration date |
| `status` | VARCHAR(32) | PENDING, ACTIVE, EXPIRED |
| `priority` | INT | Determines stacking order (lower = higher priority) |
| `quota` | JSON | - ENUM(`GLOBAL`,`PER_CUSTOMER`,`PER_ORDER`,`PER_PRODUCT`)<br>- limit |
| `quota_used` | INT | Current number of redemption |
| `created_at` | DATETIME | Record creation timestamp |
| `updated_at` | DATETIME | Last update timestamp |

### Table: `promotion_condition`

| Field | Type | Description |
|-------|------|-------------|
| `id` | INT UNSIGNED (PK) | Unique identifier of the condition |
| `rule_id` | INT UNSIGNED (FK → promotion_rule.id) | References the rule this condition belongs to |
| `condition_type` | ENUM(`QUANTITY`,`AMOUNT`,`CATEGORY`,`CUSTOMER_SEGMENT`,`CHANNEL`,`PAYMENT_METHOD`,`credit_card_type_ids`) | Type of eligibility condition |
| `threshold_value` | DECIMAL(10,2) | Minimum required quantity or amount |
| `include_product_ids` | JSON | List of SKUs or product groups that qualify |
| `include_category_ids` | JSON | List of category groups that qualify |
| `exclude_product_ids` | JSON | SKUs or groups to exclude from eligibility |
| `attributes` | JSON | Flexible key-value metadata (e.g. `{"buy_qty": 2}`) |

### Table: `promotion_action`

| Field | Type | Description |
|-------|------|-------------|
| `id` | INT UNSIGNED (PK) | Unique identifier of the reward action |
| `rule_id` | INT UNSIGNED (FK → promotion_rule.id) | References the rule this action belongs to |
| `action_type` | ENUM(`DISCOUNT_PERCENT`,`FIXED_PRICE`,`FREE_ITEM`,`FREE_SHIPPING`,`CASHBACK`) | Type of reward action |
| `discount_value` | DECIMAL(10,2) | Discount amount or percentage |
| `reward_items` | JSON | Reward SKUs or item list |
| `attributes` | JSON | Extra parameters (e.g. `{"reward_qty": 1,"fixed_price": 99}`) |

### Table: `promotion_stacking`

| Field | Type | Description |
|-------|------|-------------|
| `id` | INT UNSIGNED (PK) | Unique identifier of stacking configuration |
| `rule_id` | INT UNSIGNED (FK → promotion_rule.id) | References the rule this stacking config belongs to |
| `stackable_with` | JSON | List of template codes or rules allowed to combine |
| `exclusive_with` | JSON | List of templates / rules not allowed to combine |
| `combinable` | BOOLEAN | Whether stacking is permitted |

### Table: `promotion_result_log`

| Field | Type | Description |
|-------|------|-------------|
| `id` | INT UNSIGNED (PK) | Unique log entry identifier |
| `rule_id` | INT UNSIGNED (FK → promotion_rule.id) | Which rule produced this result |
| `basket_id` | VARCHAR(64) | ID of evaluated basket or session |
| `status` | ENUM(`APPLIED`,`NOT_APPLIED`,`PARTIAL`,`INACTIVE`) | Result status after evaluation |
| `result` | JSON | Serialized promotion result (discounts, messages, etc.) |
| `evaluated_at` | DATETIME | Evaluation timestamp |

### Relationship Summary

| Relationship | Type | Description |
|-------------|------|-------------|
| `promotion_template` → `promotion_rule` | 1 : N | One template defines many rules |
| `promotion_rule` → `promotion_condition` | 1 : N | Rule can have multiple eligibility conditions |
| `promotion_rule` → `promotion_action` | 1 : N | Rule can trigger multiple actions |
| `promotion_rule` → `promotion_stacking` | 1 : 1 | Stacking configuration per rule |
| `promotion_condition` → `product_group (product)` | N : 1 | Optional SKU/category mapping |
| `promotion_action` → `product_group (product)` | N : 1 | Optional SKU/category mapping |
| `promotion_rule` → `promotion_result_log` | 1 : N | Logs all evaluations for analytics |

## API Endpoints

### Promotion Rule Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/promotion-rules` | List promotion rules with filters (`status`, `templateId`, `active`) |
| `GET` | `/api/v1/promotion-rules/{id}` | Retrieve a single rule (includes conditions + actions) |
| `POST` | `/api/v1/promotion-rules` | Create a new promotion rule from a template |
| `PUT` | `/api/v1/promotion-rules/{id}` | Update rule config (dates, quota JSON, priority, stackable) |
| `PATCH` | `/api/v1/promotion-rules/{id}/status` | Activate / deactivate rule |
| `DELETE` | `/api/v1/promotion-rules/{id}` | Soft-delete / archive rule |

### Conditions & Actions

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/promotion-rules/{id}/conditions` | List conditions for a rule |
| `POST` | `/api/v1/promotion-rules/{id}/conditions` | Add condition (`FIRST_ORDER`, `PAYMENT_METHOD`, etc.) |
| `DELETE` | `/api/v1/promotion-conditions/{conditionId}` | Remove a condition |
| `GET` | `/api/v1/promotion-rules/{id}/actions` | List reward actions |
| `POST` | `/api/v1/promotion-rules/{id}/actions` | Add reward action (`discount`, `fixed_price`, etc.) |

### Promotion Redemption

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/promotion-engine/evaluate` | Light - full evaluate eligible promotions (no state change). Return list of eligible rule-ids |
| `POST` | `/api/v1/promotion-engine/redeem` | Light validate using list of rule-ids evaluated and cart snapshot |
| `POST` | `/api/v1/promotion-engine/redeem/{id}` | Update quota and write log |
| `DELETE` | `/api/v1/promotion-engine/redeem/{id}` | Cancel or expire confirmation (optional) |

### Stack & Quota

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/promotion-stacking` | List stacking relationships |
| `POST` | `/api/v1/promotion-stacking` | Configure allowed / excluded promotion pairs |
| `GET` | `/api/v1/promotion-rules/{id}/quota` | Get current quota JSON + usage |
| `PATCH` | `/api/v1/promotion-rules/{id}/quota` | Update quota JSON |

## Use Cases Flow

| # | Use Case | Trigger / System | Endpoint | Mode | Purpose | Engine Behavior |
|---|----------|-----------------|----------|------|---------|-----------------|
| **1** | **Add to Cart (optional)** | Cart Service or PDP | `POST /api/v1/promotion-engine/evaluate` | `light` | Preview potential promotions to support: 1. promotion recommendation 2. warm/pre-cache promotion by cart-user | Lightweight evaluation (product/spend-based only, cached by `cartHash`) |
| **2** | **Get Cart (View Cart Page)** | Cart Service | `POST /api/v1/promotion-engine/evaluate` | `medium` | Show current promotions in the cart | Same endpoint — still light evaluation, read-only, cached |
| **3** | **Open Checkout Page / Switch Shipping or Payment Method (Order Detail)** | Checkout Service | `POST /api/v1/promotion-engine/evaluate` | `full` | Confirm final applicable promotions before payment. Calculate on top of light & medium with full promotion-rules (payment, shipping, loyalty, or total bill) | Same endpoint — **full evaluation** triggered by presence of `customerId`, `paymentMethod`, etc. |
| **4** | **Press Checkout / Payment Initiated** | Checkout Service | `POST /api/v1/promotion-engine/redeem` | `create` | Lock quota (reserve) and return confirmation token. Create transaction — easy to revert and able to trace the usage | Partial re-evaluation (cart consistency, quota validation). No evaluate if cart is up to date or qty in cart is ok |
| **5** | **Payment Success / OTP Accepted** | Payment Service | `POST /api/v1/promotion-engine/redeem/{id}` | `confirm` | Finalise promotion usage and log redemption (convert from temp (4) or cache to persistence) | Updates `quota_used`, emits `PromotionRedeemedEvent` |
| **6** | **Order Fulfilment Completed (2–3 days later)** | Fulfilment / Order Service | Event: `PromotionFulfilledEvent` | `event` | Notify analytics / ROI tracking | Async event for reporting or refund reversal logic |

## Rule Index – For Better Performance

- Create rule index using the information found in 'Product-Rule' including '`include_product_ids`', '`include_category_ids`', and '`condition_type`'
- Re-caching at Schedule every hour, at application start, condition is updated
- Rule Index Example:

```
ruleIndex:
  product:               b
      1001 → [ruleId1, ruleId5]
      1002 → [ruleId3]
  category:
      2001 → [ruleId2, ruleId7]
  customerSegment:
      "FIRST_ORDER" → [ruleId8]
      "LOYALTY" -> [ruleId11]
  paymentMethod:
      "VISA" → [ruleId4]
      "COD" → [ruleId6]
```

## Services Dependencies

1. **Scheduling** → trigger to update promotion rules' status and perform caching (warm cache)
2. **Product-Service** → provide target-group for promotion_conditions for `include_product_ids, and include_category_ids`
3. **Order-Service** → orchestrator and main consumer which uses Promotion-Engine

## Upstream & Downstream

**Upstream:**
- OPAL
- eCampaign

**Downstream:**
- BigC Plus / BigC Website
- Fulfilment

## Impact Analysis (Squad or Domain Impact)

| Domain (Squad) | Description | Expected Change |
|----------------|-------------|-----------------|
| Promotion (HPC) | Introduce of new service | Scalable to support Offline Template |
| Pricing (Product) | Read discount value | - Must consume from new endpoint<br>- Backward compatible |
| Product (Product) | - Expose category / SKU<br>- Fetch promotion detail for PDP / PLP | - Must consume from new endpoint<br>- Backward compatible |
| Customer (Platform) | N/A | |
| Order (Order) | Integrate with promotion during checkout | - Must consume from new endpoint<br>- Backward compatible |
| Payment (Order) | Integrate with promotion for Templates using PaymentMethod | - Must consume from new endpoint<br>- Backward compatible |
| Fulfilment (Fulfilment) | Integrate with promotion during adjustment | - Must consume from new endpoint |
| Report BI (Data) | | |

## Non-Functional Requirements

- **Performance:** < 10ms per rule
- **Scalability:** > 1k concurrent

## Deployment Awareness

- **Require Migration from existing Promotion-Engine (Rust)**
  - Current active promotion must work as it is
  - Pending promotion must be migrated to new service
- **Beta / Alpha**
  - Close group or Whitelist users must be implemented
- **Reconfigure AWS Gateway JSON**
  - /checkout
  - /addToCart
  - /carts/detail
  - /cart/adjustment

