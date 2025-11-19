# [API] Promotion Rules - Update

Task: [API][SUCCESS] - Update Promotion Rule
// GIVEN a valid rule id and payload
// WHEN PUT /api/v1/promotion-rules/{id}
// THEN 200 with updated rule

**API Specification:**
- Endpoint: PUT /api/v1/promotion-rules/{id}
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: number 
```, body: { rule_name?: string, start_date?: string, end_date?: string, priority?: number, quota?: object } }
- Response: 200
```json
{ code: "success", data: { id: number, template_id: number, rule_name: string, start_date: string, end_date: string, active: boolean, priority: number, quota: object, quota_used: number, updated_at: string 
``` }

**Acceptance Criteria:**
- Cannot change template_id or quota_used
- Validates end_date > start_date when provided

---

Task: [API][FAIL][REQUEST] - Update Rule Request Errors
// GIVEN invalid update payload
// WHEN PUT /api/v1/promotion-rules/{id}
// THEN 400 with validation error

**API Specification:**
- Endpoint: PUT /api/v1/promotion-rules/{id}
- Authentication: JWT Bearer
- Request:
```json
{ params: { id: number 
```, body: { rule_name?: string, start_date?: string, end_date?: string, priority?: number, quota?: object } }
- Response: 400
```json
{ code: "invalid_request", message: string 
```

**Acceptance Criteria:**
- 400 for invalid dates or malformed quota JSON

---

Task: [API][FAIL][RESOURCE] - Update Rule Resource Errors
// GIVEN non-existent rule id
// WHEN PUT /api/v1/promotion-rules/{id}
// THEN 400 with resource error

**API Specification:**
- Endpoint: PUT /api/v1/promotion-rules/{id}
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

