# [API] Promotion Stacking - List

Task: [API][SUCCESS] - List Promotion Stacking Configurations
// GIVEN optional filters
// WHEN GET /api/v1/promotion-stacking
// THEN 200 with list of stacking configurations

**API Specification:**
- Endpoint: GET /api/v1/promotion-stacking?ruleId=number&combinable=boolean
- Authentication: JWT Bearer
- Request:
```json
{ query: { ruleId?: number, combinable?: boolean 
``` }
- Response: 200
```json
{ code: "success", data: { items: array 
``` }

**Acceptance Criteria:**
- Items include: id, rule_id, stackable_with, exclusive_with, combinable

---

Task: [API][FAIL][REQUEST] - List Stacking Request Errors
// GIVEN invalid filter parameters
// WHEN GET /api/v1/promotion-stacking
// THEN 400 with validation error

**API Specification:**
- Endpoint: GET /api/v1/promotion-stacking
- Authentication: JWT Bearer
- Request:
```json
{ query: { ruleId?: number, combinable?: boolean 
``` }
- Response: 400
```json
{ code: "invalid_request", message: string 
```

**Acceptance Criteria:**
- 400 for non-numeric ruleId
- 400 for invalid combinable value

