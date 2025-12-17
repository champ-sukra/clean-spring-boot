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
      "quantity": 2,
      "price": 100.00
    },
    {
      "product_id": "SKU-002",
      "category_id": "CAT-100",
      "quantity": 5,
      "price": 100.00
    }
  ],
  "payment_method": null,
  "shipping_method": null
}
```
- Response: 200
```json
{
  "code": "success",
  "data": {
    "eligible_rule_ids": [10, 15, 23],
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
  - At least one condition defined in promotion_condition table
  - Available quota (quota not exhausted: quota_used < quota.limit)
- Returns list of eligible rule IDs based on:
  - Active rules (status=2)
  - Rules within valid date range (start_date ≤ now ≤ end_date)
  - Check if cart items meet minimum quantity threshold using productCode or categoryCode list
