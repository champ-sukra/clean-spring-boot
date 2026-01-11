# [API] Promotion Engine - Evaluate

Task: [API][SUCCESS] - Evaluate Eligible Promotions

**Background:**
- ~/sequence-diagram/evaluate-promotion.puml

**OpenAPI Reference:**
- tags: [promotionEvaluation] ~/openapi/promotion-engine-api.yaml

// GIVEN valid cart/checkout context 
// AND existing active promotion rules with valid conditions
// WHEN POST /api/v1/promotion-rules/evaluate
// THEN 200 with list of eligible rule-ids (no state change)

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/evaluate
- Authentication: None
- Request Body:
```json
{
  "mode": null,
  "customer_id": "12345",
  "cart_id": "cart-abc-123",
  "items": [
    {
      "product_id": "SKU-001",
      "category_id": "CAT-100",
      "quantity": 6,
      "price": 100.00
    },
    {
      "product_id": "SKU-002",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 400.00
    },
    {
      "product_id": "SKU-004",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 100.00
    },
    {
      "product_id": "SKU-011",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 150.00
    },
    {
      "product_id": "SKU-012",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 150.00
    },
    {
      "product_id": "SKU-013",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 150.00
    },
    {
      "product_id": "SKU-014",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 150.00
    },
    {
      "product_id": "SKU-015",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 150.00
    },
    {
      "product_id": "SKU-016",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 150.00
    },
    {
      "product_id": "SKU-017",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 150.00
    },
    {
      "product_id": "SKU-018",
      "category_id": "LACTASOY",
      "quantity": 5,
      "price": 150.00
    },
    {
      "product_id": "SKU-019",
      "category_id": "MILO",
      "quantity": 10,
      "price": 150.00
    },
    {
      "product_id": "BESICO-1",
      "category_id": "BJC-115",
      "quantity": 4,
      "price": 150.00
    },
    {
      "product_id": "BESICO-2",
      "category_id": "BJC-115",
      "quantity": 4,
      "price": 150.00
    },
    {
      "product_id": "PROD001",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 150.00
    },
    {
      "product_id": "COKE-55555",
      "category_id": "C-115",
      "quantity": 3,
      "price": 100.00
    }
  ],
  "payment_method": "CREDIT_CARD",
  "shipping_method": null
}
```
- Response: 200
```json
{
  "code": "success",
  "data": {
    "eligible_rule_ids": [{
      "rule_id": 12345,
      "rule_name": "Buy 2 get 1 free",
      "coupon_code": "asdasdasd"
    }, {
      "rule_id": 12346,
      "rule_name": "Buy 3 get 2 free",
      "coupon_code": "334234asd"
    }],
    "evaluated_at": "2025-11-26T10:30:00Z"
  }
}
```

**Response Field Descriptions:**
- `eligible_rule_ids`: array of integers - List of promotion rule IDs that the cart is eligible for
- `evaluated_at`: string (ISO 8601) - Timestamp of evaluation

**Acceptance Criteria:**
- No state change (read-only operation)
- Requires existing promotion rules in database with:
  - Valid template reference (template_id exists and template is active)
  - Status = 2 (ACTIVE)
  - Current datetime within range (start_date ≤ now ≤ end_date)
  - Valid promotion rule with using TPL_COUPON (coupon_code exists and coupon is active)
  - At least one condition defined in promotion_condition table
  - Available quota (quota not exhausted: quota_used < quota.limit)
- Returns list of eligible rule IDs based on:
  - Active rules (status=2)
  - Rules within valid date range (start_date ≤ now ≤ end_date)
  - Eligible rules if cart items, which match the rule's conditions both
