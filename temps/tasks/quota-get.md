# [API] Promotion Quota - Get

Task: [API][SUCCESS] - Get Promotion Rule Quota
// GIVEN a valid rule id
// WHEN GET /api/v1/promotion-rules/{id}/quota
// THEN 200 with quota and usage

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}/quota
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: number 
``` }
- Response: 200
```json
{ code: "success", data: { rule_id: number, quota: object, quota_used: number, remaining: number 
``` }

**Acceptance Criteria:**
- Returns quota JSON and quota_used; calculates remaining

---

Task: [API][FAIL][RESOURCE] - Get Quota Resource Errors
// GIVEN non-existent rule id
// WHEN GET /api/v1/promotion-rules/{id}/quota
// THEN 400 with resource not found

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}/quota
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: number 
``` }
- Response: 400
```json
{ code: "resource_not_found", message: string 
```

**Acceptance Criteria:**
- 400 when rule id not found

