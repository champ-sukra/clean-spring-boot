# [API] Promotion Engine - Redeem (Create)

Task: [API][SUCCESS] - Redeem Promotions (Create)

// GIVEN eligible rules and cart snapshot
// WHEN POST /api/v1/promotion-engine/redeem
// THEN 200 with redemption token

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/redeem
- Authentication: JWT Bearer
- Request:
```json
{
  "rule_ids": "array",
  "cart": "object",
  "customer_id": "string (optional)"
}
```
- Response: 200
```json
{
  "code": "success",
  "data": {
    "redemption_id": "string",
    "rule_ids": "array",
    "reserved_at": "string",
    "expires_at": "string"
  }
}
```

**Acceptance Criteria:**
- Locks quota without incrementing quota_used
- Expires reservation automatically

---

Task: [API][FAIL][REQUEST] - Redeem Create Request Errors

// GIVEN invalid payload
// WHEN POST /api/v1/promotion-engine/redeem
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/redeem
- Authentication: JWT Bearer
- Request:
```json
{
  "rule_ids": "array (optional)",
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
- 400 for missing rule_ids or cart

---

Task: [API][FAIL][QUOTA] - Redeem Create Quota Errors

// GIVEN quota exceeded or inactive/expired rules
// WHEN POST /api/v1/promotion-engine/redeem
// THEN 400 with quota/unavailable error

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/redeem
- Authentication: JWT Bearer
- Request:
```json
{
  "rule_ids": "array",
  "cart": "object"
}
```
- Response: 400
```json
{
  "code": "quota_exceeded | promotion_unavailable",
  "message": "string"
}
```

**Acceptance Criteria:**
- 400 when any rule quota exhausted or inactive/expired

