# Promotion Engine (Evaluate & Redeem) - Tasks (API Only)

Task: [API][SUCCESS] - Evaluate Promotions (Light)
// GIVEN a cart for light evaluation
// WHEN POST /api/v1/promotion-engine/evaluate with mode=light
// THEN 200 with eligible rule IDs

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/evaluate
- Authentication: JWT Bearer
- Request: { mode: string, cart: object, cartHash?: string }
- Response: 200 { code: "success", data: { eligible_rules: array, preview: object } }

**Acceptance Criteria:**
- Evaluates product/spend-based rules
- Uses cache by cartHash when provided

---

Task: [API][SUCCESS] - Evaluate Promotions (Full)
// GIVEN full checkout context
// WHEN POST /api/v1/promotion-engine/evaluate with mode=full
// THEN 200 with eligible rule IDs and totals

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/evaluate
- Authentication: JWT Bearer
- Request: { mode: string, cart: object, customer_id?: string, payment_method?: string, shipping_method?: string, channel?: string }
- Response: 200 { code: "success", data: { eligible_rules: array, preview: object, total_discount: number } }

**Acceptance Criteria:**
- Checks all condition types and stacking
- Respects rule priority ordering

---

Task: [API][SUCCESS] - Redeem Promotions (Create)
// GIVEN eligible rules and cart snapshot
// WHEN POST /api/v1/promotion-engine/redeem
// THEN 200 with redemption token

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/redeem
- Authentication: JWT Bearer
- Request: { rule_ids: array, cart: object, customer_id?: string }
- Response: 200 { code: "success", data: { redemption_id: string, rule_ids: array, reserved_at: string, expires_at: string } }

**Acceptance Criteria:**
- Locks quota without incrementing quota_used
- Sets expiration time for reservation

---

Task: [API][SUCCESS] - Confirm Redemption
// GIVEN a valid redemption token
// WHEN POST /api/v1/promotion-engine/redeem/{id}
// THEN 200 with confirmation

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/redeem/{id}
- Authentication: JWT Bearer
- Request: { params: { id: string } }
- Response: 200 { code: "success", data: { redemption_id: string, rule_ids: array, confirmed_at: string, quota_updated: boolean } }

**Acceptance Criteria:**
- Increments quota_used and logs result

---

Task: [API][SUCCESS] - Cancel Redemption
// GIVEN a valid unconfirmed token
// WHEN DELETE /api/v1/promotion-engine/redeem/{id}
// THEN 200 with cancellation result

**API Specification:**
- Endpoint: DELETE /api/v1/promotion-engine/redeem/{id}
- Authentication: JWT Bearer
- Request: { params: { id: string } }
- Response: 200 { code: "success", data: { redemption_id: string, cancelled: boolean, quota_released: boolean } }

**Acceptance Criteria:**
- Releases reserved quota if not expired

---

Task: [API][FAIL][REQUEST] - Evaluate Request Errors
// GIVEN invalid evaluate payload
// WHEN POST /api/v1/promotion-engine/evaluate
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/evaluate
- Authentication: JWT Bearer
- Request: { mode?: string, cart?: object }
- Response: 400 { code: "invalid_request", message: string }

**Acceptance Criteria:**
- 400 for missing mode or cart
- 400 for invalid mode value

---

Task: [API][FAIL][QUOTA] - Redemption Quota Errors
// GIVEN quota exhaustion or violation
// WHEN POST /api/v1/promotion-engine/redeem
// THEN 400 with quota error

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/redeem
- Authentication: JWT Bearer
- Request: { rule_ids: array, cart: object }
- Response: 400 { code: "quota_exceeded" | "promotion_unavailable", message: string }

**Acceptance Criteria:**
- 400 when rule quota exhausted
- 400 when rule inactive/expired

---

Task: [API][FAIL][RESOURCE] - Redemption Resource Errors
// GIVEN non-existent/expired token
// WHEN POST/DELETE /api/v1/promotion-engine/redeem/{id}
// THEN 400 with resource error

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/redeem/{id} | DELETE /api/v1/promotion-engine/redeem/{id}
- Authentication: JWT Bearer
- Request: { params: { id: string } }
- Response: 400 { code: "resource_not_found" | "redemption_expired", message: string }

**Acceptance Criteria:**
- 400 for invalid or expired id
