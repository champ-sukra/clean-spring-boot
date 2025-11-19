# [API] Promotion Quota - Update

Task: [API][SUCCESS] - Update Promotion Rule Quota
// GIVEN a valid rule id and quota array
// WHEN PATCH /api/v1/promotion-rules/{id}/quota
// THEN 200 with updated quota

**API Specification:**
- Endpoint: PATCH /api/v1/promotion-rules/{id}/quota
- Authentication: JWT Bearer
- Request:
```json
{ quota: array 
```
- Response: 200
```json
{ code: "success", data: { rule_id: number, quota: object, quota_used: number, updated_at: string 
``` }

**Acceptance Criteria:**
- Validates quota array structure and ENUM types
- Does not modify quota_used

---

Task: [API][FAIL][REQUEST] - Update Quota Request Errors
// GIVEN invalid quota payload
// WHEN PATCH /api/v1/promotion-rules/{id}/quota
// THEN 400 with validation error

**API Specification:**
- Endpoint: PATCH /api/v1/promotion-rules/{id}/quota
- Authentication: JWT Bearer
- Request:
```json
{ quota?: array 
```
- Response: 400
```json
{ code: "invalid_request", message: string 
```

**Acceptance Criteria:**
- 400 for missing quota or invalid types

