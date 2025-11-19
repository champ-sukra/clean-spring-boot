# [API] Promotion Rules - Delete

Task: [API][SUCCESS] - Delete Promotion Rule
// GIVEN a valid rule id
// WHEN DELETE /api/v1/promotion-rules/{id}
// THEN 200 with deletion confirmation

**API Specification:**
- Endpoint: DELETE /api/v1/promotion-rules/{id}
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: number 
``` }
- Response: 200
```json
{ code: "success", data: { id: number, deleted: boolean 
``` }

**Acceptance Criteria:**
- Soft-delete/archive rule

---

Task: [API][FAIL][RESOURCE] - Delete Rule Resource Errors
// GIVEN non-existent rule id
// WHEN DELETE /api/v1/promotion-rules/{id}
// THEN 400 with resource not found

**API Specification:**
- Endpoint: DELETE /api/v1/promotion-rules/{id}
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

