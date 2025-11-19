# [API] Promotion Stacking - Create

Task: [API][SUCCESS] - Create Promotion Stacking Configuration
// GIVEN a valid rule id and stacking data
// WHEN POST /api/v1/promotion-stacking
// THEN 200 with created config

**API Specification:**
- Endpoint: POST /api/v1/promotion-stacking
- Authentication: JWT Bearer
- Request:
```json
{ rule_id: number, stackable_with?: array, exclusive_with?: array, combinable: boolean 
```
- Response: 200
```json
{ code: "success", data: { id: number, rule_id: number, stackable_with: array, exclusive_with: array, combinable: boolean 
``` }

**Acceptance Criteria:**
- One config per rule (rule_id unique)
- Validate arrays don't overlap

---

Task: [API][FAIL][REQUEST] - Create Stacking Request Errors
// GIVEN invalid stacking payload
// WHEN POST /api/v1/promotion-stacking
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-stacking
- Authentication: JWT Bearer
- Request:
```json
{ rule_id?: number, stackable_with?: array, exclusive_with?: array, combinable?: boolean 
```
- Response: 400
```json
{ code: "invalid_request", message: string 
```

**Acceptance Criteria:**
- 400 for missing rule_id/combinable
- 400 for malformed arrays or overlaps

---

Task: [API][FAIL][RESOURCE] - Create Stacking Resource Errors
// GIVEN non-existent rule or duplicate config
// WHEN POST /api/v1/promotion-stacking
// THEN 400 with resource error

**API Specification:**
- Endpoint: POST /api/v1/promotion-stacking
- Authentication: JWT Bearer
- Request:
```json
{ rule_id: number 
```
- Response: 400
```json
{ code: "resource_not_found" | "duplicate_resource", message: string 
```

**Acceptance Criteria:**
- 400 when rule_id not found
- 400 when config already exists

