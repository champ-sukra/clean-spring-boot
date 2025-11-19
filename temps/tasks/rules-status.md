# [API] Promotion Rules - Update Status

Task: [API][SUCCESS] - Update Promotion Rule Status
// GIVEN a valid rule id
// WHEN PATCH /api/v1/promotion-rules/{id}/status
// THEN 200 with updated status

**API Specification:**
- Endpoint: PATCH /api/v1/promotion-rules/{id}/status
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: number 
```, body: { active: boolean } }
- Response: 200
```json
{ code: "success", data: { id: number, active: boolean, updated_at: string 
``` }

**Acceptance Criteria:**
- Toggles active flag only

---

Task: [API][FAIL][REQUEST] - Update Status Request Errors
// GIVEN invalid payload
// WHEN PATCH /api/v1/promotion-rules/{id}/status
// THEN 400 with validation error

**API Specification:**
- Endpoint: PATCH /api/v1/promotion-rules/{id}/status
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: number 
```, body: { active?: boolean } }
- Response: 400
```json
{ code: "invalid_request", message: string 
```

**Acceptance Criteria:**
- 400 for missing active

---

Task: [API][FAIL][RESOURCE] - Update Status Resource Errors
// GIVEN non-existent rule id
// WHEN PATCH /api/v1/promotion-rules/{id}/status
// THEN 400 with resource not found

**API Specification:**
- Endpoint: PATCH /api/v1/promotion-rules/{id}/status
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

