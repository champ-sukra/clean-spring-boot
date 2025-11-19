# [API] Promotion Conditions - List

Task: [API][SUCCESS] - List Promotion Conditions
// GIVEN a valid rule id
// WHEN GET /api/v1/promotion-rules/{id}/conditions
// THEN 200 with list of conditions

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}/conditions
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
- Items include: id, rule_id, condition_type, threshold_value, include_product_ids, include_category_ids, exclude_product_ids, attributes

---

Task: [API][FAIL][RESOURCE] - List Conditions Resource Errors
// GIVEN non-existent rule id
// WHEN GET /api/v1/promotion-rules/{id}/conditions
// THEN 400 with resource not found

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}/conditions
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

