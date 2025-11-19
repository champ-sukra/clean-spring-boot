# [API] Promotion Conditions - Delete

Task: [API][SUCCESS] - Delete Promotion Condition
// GIVEN a valid condition id
// WHEN DELETE /api/v1/promotion-conditions/{conditionId}
// THEN 200 with deletion confirmation

**API Specification:**
- Endpoint: DELETE /api/v1/promotion-conditions/{conditionId}
- Authentication: JWT Bearer
- Request:
```json
{ params: { conditionId: number 
``` }
- Response: 200
```json
{ code: "success", data: { id: number, deleted: boolean 
``` }

**Acceptance Criteria:**
- Only deletes the condition entry

---

Task: [API][FAIL][RESOURCE] - Delete Condition Resource Errors
// GIVEN non-existent condition id
// WHEN DELETE /api/v1/promotion-conditions/{conditionId}
// THEN 400 with resource not found

**API Specification:**
- Endpoint: DELETE /api/v1/promotion-conditions/{conditionId}
- Authentication: JWT Bearer
- Request:
```json
{ params: { conditionId: number 
``` }
- Response: 400
```json
{ code: "resource_not_found", message: string 
```

**Acceptance Criteria:**
- 400 when condition id not found

