# [API] Promotion Engine - Redeem (Cancel)

Task: [API][SUCCESS] - Cancel Redemption
// GIVEN a valid unconfirmed token
// WHEN DELETE /api/v1/promotion-engine/redeem/{id}
// THEN 200 with cancellation result

**API Specification:**
- Endpoint: DELETE /api/v1/promotion-engine/redeem/{id}
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: string 
``` }
- Response: 200
```json
{ code: "success", data: { redemption_id: string, cancelled: boolean, quota_released: boolean 
``` }

**Acceptance Criteria:**
- Releases reserved quota if not expired

---

Task: [API][FAIL][RESOURCE] - Cancel Redemption Resource Errors
// GIVEN non-existent or expired token
// WHEN DELETE /api/v1/promotion-engine/redeem/{id}
// THEN 400 with resource error

**API Specification:**
- Endpoint: DELETE /api/v1/promotion-engine/redeem/{id}
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
- 400 for invalid/expired id

