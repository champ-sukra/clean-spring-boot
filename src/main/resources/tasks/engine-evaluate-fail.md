# [API] Promotion Engine - Evaluate

Task: [API][FAIL][REQUEST] - Evaluate Request Validation Errors

// GIVEN invalid request body
// WHEN POST /api/v1/promotion-engine/evaluate
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/evaluate
- Authentication: JWT Bearer
- Possible Invalid Cases:
  - Missing required fields (mode, cartId, items, totalAmount)
  - Invalid mode value (not in: light, medium, full)
  - Empty items array
  - Invalid item quantity (< 1)
  - Invalid price or totalAmount (< 0)
  - Missing customerId when mode=full
  - Missing paymentMethod when mode=full and payment-based rules exist
- Response: 400
```json
{
  "code": "invalid_request",
  "message": "mode is required and must be one of: light, medium, full"
}
```
Another example:
```json
{
  "code": "invalid_request",
  "message": "customerId is required for full evaluation mode"
}
```
Another example:
```json
{
  "code": "invalid_request",
  "message": "items cannot be empty"
}
```

**Acceptance Criteria:**
- 400 for missing required fields
- 400 for invalid mode value
- 400 for empty items array
- 400 for invalid numeric values (quantity, price, totalAmount)
- 400 for missing context-specific fields (customerId for full mode)

---

Task: [API][FAIL][BUSINESS] - No Eligible Promotions

// GIVEN valid request but no eligible promotions
// WHEN POST /api/v1/promotion-engine/evaluate
// THEN 200 with empty eligibleRuleIds array

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/evaluate
- Authentication: JWT Bearer
- Response: 200
```json
{
  "code": "success",
  "data": {
    "eligibleRuleIds": [],
    "evaluationMode": "light",
    "cartHash": "a1b2c3d4e5f6",
    "evaluatedAt": "2025-11-26T10:30:00Z"
  }
}
```

**Acceptance Criteria:**
- Returns success with empty array when:
  - No active promotion rules match the cart
  - All matching rules have exhausted quota
  - Cart doesn't meet minimum conditions for any rule
  - All matching rules are outside valid date range
- Still returns valid cartHash and evaluatedAt

---

**Notes:**
- This is a read-only evaluation endpoint - no state changes
- Used across multiple use cases (Add to Cart, View Cart, Checkout)
- Evaluation depth controlled by `mode` parameter
- Results cacheable by `cartHash` for performance
- Stacking rules applied during evaluation to exclude mutually exclusive promotions
- Priority ordering applied if multiple rules are eligible
- Quota validation performed but not decremented

