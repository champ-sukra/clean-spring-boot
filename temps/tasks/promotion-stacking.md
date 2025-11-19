# Promotion Stacking - Tasks (API Only)

Task: [API][SUCCESS] - List Promotion Stacking Configurations
// GIVEN optional filters
// WHEN GET /api/v1/promotion-stacking
// THEN 200 with list of stacking configurations

**API Specification:**
- Endpoint: GET /api/v1/promotion-stacking?ruleId=number&combinable=boolean
- Authentication: JWT Bearer
- Request: { query: { ruleId?: number, combinable?: boolean } }
- Response: 200 { code: "success", data: { items: array } }

**Acceptance Criteria:**
- Items include: id, rule_id, stackable_with, exclusive_with, combinable
- Filters by ruleId and combinable when provided

---

Task: [API][SUCCESS] - Create Promotion Stacking Configuration
// GIVEN a valid rule id and stacking data
// WHEN POST /api/v1/promotion-stacking
// THEN 200 with created config

**API Specification:**
- Endpoint: POST /api/v1/promotion-stacking
- Authentication: JWT Bearer
- Request: { rule_id: number, stackable_with?: array, exclusive_with?: array, combinable: boolean }
- Response: 200 { code: "success", data: { id: number, rule_id: number, stackable_with: array, exclusive_with: array, combinable: boolean } }

**Acceptance Criteria:**
- One config per rule (rule_id unique)
- Validate arrays don't overlap

---

Task: [API][FAIL][REQUEST] - Promotion Stacking Request Errors
// GIVEN invalid stacking payload
// WHEN POST /api/v1/promotion-stacking
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-stacking
- Authentication: JWT Bearer
- Request: { rule_id?: number, stackable_with?: array, exclusive_with?: array, combinable?: boolean }
- Response: 400 { code: "invalid_request", message: string }

**Acceptance Criteria:**
- 400 for missing rule_id/combinable
- 400 for malformed arrays
- 400 for overlapping arrays

---

Task: [API][FAIL][RESOURCE] - Promotion Stacking Resource Errors
// GIVEN non-existent or duplicate configuration
// WHEN operating on promotion stacking
// THEN 400 with resource error

**API Specification:**
- Endpoint: POST /api/v1/promotion-stacking
- Authentication: JWT Bearer
- Request: { rule_id: number }
- Response: 400 { code: "resource_not_found" | "duplicate_resource", message: string }

**Acceptance Criteria:**
- 400 when rule_id not found
- 400 when config already exists for rule
