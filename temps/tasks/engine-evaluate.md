# [API] Promotion Engine - Evaluate

Task: [API][SUCCESS] - Evaluate Promotions

// GIVEN a cart and evaluation mode
// WHEN POST /api/v1/promotion-engine/evaluate
// THEN 200 with eligible rule IDs and preview

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/evaluate
- Authentication: JWT Bearer
- Request:
```json
{
  "mode": "string (light|medium|full)",
  "cart": "object",
  "cartHash": "string (optional)",
  "customer_id": "string (optional)",
  "payment_method": "string (optional)",
  "shipping_method": "string (optional)",
  "channel": "string (optional)"
}
```
- Response: 200
```json
{
  "code": "success",
  "data": {
    "eligible_rules": "array",
    "preview": "object",
    "total_discount": "number (optional)"
  }
}
```

**Acceptance Criteria:**
- mode: light|medium|full
- Applies stacking and priority when full context provided

---

Task: [API][FAIL][REQUEST] - Evaluate Request Errors

// GIVEN invalid evaluate payload
// WHEN POST /api/v1/promotion-engine/evaluate
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/evaluate
- Authentication: JWT Bearer
- Request:
```json
{
  "mode": "string (optional)",
  "cart": "object (optional)"
}
```
- Response: 400
```json
{
  "code": "invalid_request",
  "message": "string"
}
```

**Acceptance Criteria:**
- 400 for missing mode or cart
- 400 for invalid mode value

