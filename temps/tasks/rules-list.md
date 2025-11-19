# [API] Promotion Rules - List
- 400 for non-numeric templateId/page/size
- 400 for invalid status value
**Acceptance Criteria:**

- Response: 400 { code: "invalid_request", message: string }
- Request: { query: { status?: string, templateId?: number, active?: boolean, page?: number, size?: number } }
- Authentication: JWT Bearer
- Endpoint: GET /api/v1/promotion-rules
**API Specification:**

// THEN 400 with validation error
// WHEN GET /api/v1/promotion-rules
// GIVEN invalid filter parameters
Task: [API][FAIL][REQUEST] - List Rules Request Errors

---

- Items include rule summary fields
- Pagination defaults: page=1, size=20
- Filters: status (ACTIVE|INACTIVE|EXPIRED), templateId, active
**Acceptance Criteria:**

- Response: 200 { code: "success", data: { items: array, total: number, page: number, size: number } }
- Request: { query: { status?: string, templateId?: number, active?: boolean, page?: number, size?: number } }
- Authentication: JWT Bearer
- Endpoint: GET /api/v1/promotion-rules?status=string&templateId=number&active=boolean&page=number&size=number
**API Specification:**

// THEN 200 with filtered, paginated list of rules
// WHEN GET /api/v1/promotion-rules
// GIVEN valid optional filter parameters
Task: [API][SUCCESS] - List Promotion Rules


