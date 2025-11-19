# Promotion Rules - Tasks (API Only)

Task: [API][SUCCESS] - List Promotion Rules
// GIVEN valid optional filter parameters
// WHEN GET /api/v1/promotion-rules
// THEN 200 with filtered, paginated list of rules

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules?status=string&templateId=number&active=boolean&page=number&size=number
- Authentication: JWT Bearer
- Request: { query: { status?: string, templateId?: number, active?: boolean, page?: number, size?: number } }
- Response: 200 { code: "success", data: { items: array, total: number, page: number, size: number } }

**Acceptance Criteria:**
- Supports filters: status (ACTIVE|INACTIVE|EXPIRED), templateId, active
- Pagination defaults: page=1, size=20
- Items include: id, template_id, rule_name, start_date, end_date, active, priority, quota, quota_used
- Returns empty items when no matches

---

Task: [API][SUCCESS] - Get Promotion Rule by ID
// GIVEN an existing rule id
// WHEN GET /api/v1/promotion-rules/{id}
// THEN 200 with rule details (conditions, actions, stacking)

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}
- Authentication: JWT Bearer
- Request: { params: { id: number } }
- Response: 200 { code: "success", data: { id: number, template_id: number, rule_name: string, start_date: string, end_date: string, active: boolean, priority: number, quota: object, quota_used: number, conditions: array, actions: array, stacking: object, created_at: string, updated_at: string } }

**Acceptance Criteria:**
- Includes arrays: conditions[], actions[]
- Includes stacking object or null
- 400 when rule not found

---

Task: [API][SUCCESS] - Create Promotion Rule
// GIVEN a valid template_id and configuration
// WHEN POST /api/v1/promotion-rules
// THEN 200 with created rule

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules
- Authentication: JWT Bearer
- Request: { template_id: number, rule_name: string, start_date: string, end_date: string, active?: boolean, priority?: number, quota?: object }
- Response: 200 { code: "success", data: { id: number, template_id: number, rule_name: string, start_date: string, end_date: string, active: boolean, priority: number, quota: object, quota_used: number, created_at: string, updated_at: string } }

**Acceptance Criteria:**
- Validates template_id exists
- Initializes quota_used=0; sets timestamps
- end_date must be after start_date

---

Task: [API][SUCCESS] - Update Promotion Rule
// GIVEN a valid rule id and payload
// WHEN PUT /api/v1/promotion-rules/{id}
// THEN 200 with updated rule

**API Specification:**
- Endpoint: PUT /api/v1/promotion-rules/{id}
- Authentication: JWT Bearer
- Request: { params: { id: number }, body: { rule_name?: string, start_date?: string, end_date?: string, priority?: number, quota?: object } }
- Response: 200 { code: "success", data: { id: number, template_id: number, rule_name: string, start_date: string, end_date: string, active: boolean, priority: number, quota: object, quota_used: number, updated_at: string } }

**Acceptance Criteria:**
- Cannot change template_id or quota_used
- Validates end_date > start_date when provided
- Updates updated_at

---

Task: [API][SUCCESS] - Update Promotion Rule Status
// GIVEN a valid rule id
// WHEN PATCH /api/v1/promotion-rules/{id}/status
// THEN 200 with updated status

**API Specification:**
- Endpoint: PATCH /api/v1/promotion-rules/{id}/status
- Authentication: JWT Bearer
- Request: { params: { id: number }, body: { active: boolean } }
- Response: 200 { code: "success", data: { id: number, active: boolean, updated_at: string } }

**Acceptance Criteria:**
- Toggles active flag only
- 400 when rule not found

---

Task: [API][SUCCESS] - Delete Promotion Rule
// GIVEN a valid rule id
// WHEN DELETE /api/v1/promotion-rules/{id}
// THEN 200 with deletion confirmation

**API Specification:**
- Endpoint: DELETE /api/v1/promotion-rules/{id}
- Authentication: JWT Bearer
- Request: { params: { id: number } }
- Response: 200 { code: "success", data: { id: number, deleted: boolean } }

**Acceptance Criteria:**
- Soft-delete/archive rule
- Removes from active listings

---

Task: [API][FAIL][REQUEST] - Promotion Rule Request Errors
// GIVEN missing or invalid input
// WHEN POST/PUT /api/v1/promotion-rules
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules | PUT /api/v1/promotion-rules/{id}
- Authentication: JWT Bearer
- Request: { template_id?: number, rule_name?: string, start_date?: string, end_date?: string, priority?: number, quota?: object }
- Response: 400 { code: "invalid_request", message: string }

**Acceptance Criteria:**
- 400 for missing required fields (create)
- 400 for invalid date format or end_date <= start_date
- 400 for malformed quota JSON

---

Task: [API][FAIL][RESOURCE] - Promotion Rule Resource Errors
// GIVEN invalid or missing resources
// WHEN operating on rules
// THEN 400 with resource error

**API Specification:**
- Endpoint: GET/PUT/DELETE /api/v1/promotion-rules/{id} | POST /api/v1/promotion-rules
- Authentication: JWT Bearer
- Request: { params?: { id?: number }, body?: object }
- Response: 400 { code: "resource_not_found" | "duplicate_resource", message: string }

**Acceptance Criteria:**
- 400 when rule id not found
- 400 when template_id invalid
- 400 when duplicate rule_name within same template
