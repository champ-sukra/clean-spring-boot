# [API] Promotion Engine - Evaluate

Task: [API][SUCCESS] - Evaluate Eligible Promotions

// GIVEN valid cart/checkout context 
// AND existing active promotion rules with valid conditions
// WHEN POST /api/v1/promotion-engine/evaluate
// THEN 200 with list of eligible rule-ids (no state change)

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/evaluate
- Authentication: None
- Request Body:
```json
{
  "mode": "light",
  "customerId": "12345",
  "cartId": "cart-abc-123",
  "items": [
    {
      "productId": "SKU-001",
      "categoryId": "CAT-100",
      "quantity": 2,
      "price": 100.00
    }
  ],
  "paymentMethod": null,
  "shippingMethod": null
}
```
- Response: 200
```json
{
  "code": "success",
  "data": {
    "eligibleRuleIds": [10, 15, 23],
    "evaluationMode": "light",
    "evaluatedAt": "2025-11-26T10:30:00Z"
  }
}
```

**Request Field Descriptions:
- `mode`: string (required) - Evaluation mode: `light`, `medium`, or `full`
  - `light`: Product/spend-based only (Add to Cart, View Cart)
  - `medium`: Same as light but may include more cart context
  - `full`: Complete evaluation including payment, shipping, loyalty (Checkout Page)
- `customerId`: string (optional) - Required for `full` mode or customer-specific promotions
- `cartId`: string (required) - Cart or session identifier
- `items`: array (required) - Cart items
  - `productId`: string (required) - SKU identifier
  - `categoryId`: string (optional) - Category identifier
  - `quantity`: integer (required, minimum=1) - Item quantity
  - `price`: number (required, minimum=0) - Unit price
- `paymentMethod`: string (optional) - Required for `full` mode if payment method affects eligibility
- `shippingMethod`: string (optional) - Required for `full` mode if shipping affects eligibility

**Response Field Descriptions:**
- `eligibleRuleIds`: array of integers - List of promotion rule IDs that the cart is eligible for
- `evaluationMode`: string - Echo of the requested evaluation mode
- `evaluatedAt`: string (ISO 8601) - Timestamp of evaluation

**Acceptance Criteria:**
- No state change (read-only operation)
- Requires existing promotion rules in database with:
  - Valid template reference (template_id exists and template is active)
  - Status = 2 (ACTIVE)
  - Current datetime within range (start_date ≤ now ≤ end_date)
  - At least one condition defined in promotion_condition table
  - At least one action defined in promotion_action table
  - Available quota (quota not exhausted: quota_used < quota.limit)
- Returns list of eligible rule IDs based on:
  - Active rules (status=2)
  - Rules within valid date range (start_date ≤ now ≤ end_date)
  - Matching conditions from promotion_condition table:
    - QUANTITY: cart items meet minimum quantity threshold
    - AMOUNT: calculated total (sum of item.quantity * item.price) meets minimum threshold_value
    - CATEGORY: cart items match include_category_ids (exclude exclude_product_ids)
    - CUSTOMER_SEGMENT: customerId matches segment criteria (if applicable)
    - PAYMENT_METHOD: paymentMethod matches condition criteria (for full mode)
    - CHANNEL: applicable channel criteria
  - Available quota (quota not exhausted)
  - Stacking rules from promotion_stacking table (if multiple rules evaluated)
- Evaluation mode determines depth:
  - `light`: Product SKU, category, quantity, amount conditions only
  - `medium`: Same as light with additional cart context
  - `full`: All conditions including payment method, shipping, customer segment, loyalty
- Performance target: < 10ms per rule

---

