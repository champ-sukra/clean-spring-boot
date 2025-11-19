# Promotion Quota - Tasks (API Only)

Task: [API][SUCCESS] - Get Promotion Rule Quota
// GIVEN a valid rule id
// WHEN GET /api/v1/promotion-rules/{id}/quota
// THEN 200 with quota and usage

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}/quota
- Authentication: JWT Bearer
- Request: { params: { id: number } }
- Response: 200 { code: "success", data: { rule_id: number, quota: object, quota_used: number, remaining: number } }

**Acceptance Criteria:**
- Returns quota JSON and quota_used
- Calculates remaining quota

---

Task: [API][SUCCESS] - Update Promotion Rule Quota
// GIVEN a valid rule id and quota array
// WHEN PATCH /api/v1/promotion-rules/{id}/quota
// THEN 200 with updated quota

**API Specification:**
- Endpoint: PATCH /api/v1/promotion-rules/{id}/quota
- Authentication: JWT Bearer
- Request: { quota: array }
- Response: 200 { code: "success", data: { rule_id: number, quota: object, quota_used: number, updated_at: string } }

**Acceptance Criteria:**
- Validates quota array structure and ENUM types
- Does not modify quota_used directly

---

Task: [API][FAIL][REQUEST] - Promotion Quota Request Errors
// GIVEN invalid quota payload
// WHEN PATCH /api/v1/promotion-rules/{id}/quota
// THEN 400 with validation error

**API Specification:**
- Endpoint: PATCH /api/v1/promotion-rules/{id}/quota
- Authentication: JWT Bearer
- Request: { quota?: array }
- Response: 400 { code: "invalid_request", message: string }

**Acceptance Criteria:**
- 400 for missing quota
- 400 for malformed array or invalid types
