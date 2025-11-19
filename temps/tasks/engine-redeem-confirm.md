# [API] Promotion Engine - Redeem (Confirm)

Task: [API][SUCCESS] - Confirm Redemption
// GIVEN a valid redemption token
// WHEN POST /api/v1/promotion-engine/redeem/{id}
// THEN 200 with confirmation

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/redeem/{id}
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: string 
``` }
- Response: 200
```json
{ code: "success", data: { redemption_id: string, rule_ids: array, confirmed_at: string, quota_updated: boolean 
``` }

**Acceptance Criteria:**
- Increments quota_used; writes result log

---

Task: [API][FAIL][RESOURCE] - Confirm Redemption Resource Errors
// GIVEN non-existent or expired token
// WHEN POST /api/v1/promotion-engine/redeem/{id}
// THEN 400 with resource error

**API Specification:**
- Endpoint: POST /api/v1/promotion-engine/redeem/{id}
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: string 
``` }
- Response: 400
```json
{ code: "resource_not_found" | "redemption_expired", message: string 
```

**Acceptance Criteria:**
- 400 for invalid/expired id or already confirmed

