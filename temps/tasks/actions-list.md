# [API] Promotion Actions - List

Task: [API][SUCCESS] - List Promotion Actions
// GIVEN a valid rule id
// WHEN GET /api/v1/promotion-rules/{id}/actions
// THEN 200 with list of actions

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}/actions
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: number 
``` }
- Response: 200
```json
{ code: "success", data: { items: array 
``` }

**Acceptance Criteria:**
- Items include: id, rule_id, action_type, discount_value, reward_items, attributes

---

Task: [API][FAIL][RESOURCE] - List Actions Resource Errors
// GIVEN non-existent rule id
// WHEN GET /api/v1/promotion-rules/{id}/actions
// THEN 400 with resource not found

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}/actions
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

