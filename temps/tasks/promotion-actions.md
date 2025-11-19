# Promotion Actions - Tasks (API Only)

Task: [API][SUCCESS] - List Promotion Actions
// GIVEN a valid rule id
// WHEN GET /api/v1/promotion-rules/{id}/actions
// THEN 200 with list of actions

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}/actions
- Authentication: JWT Bearer
- Request: { params: { id: number } }
- Response: 200 { code: "success", data: { items: array } }

**Acceptance Criteria:**
- Returns array of actions for rule id
- Items include: id, rule_id, action_type, discount_value, reward_items, attributes

---

Task: [API][SUCCESS] - Create Promotion Action
// GIVEN a valid rule id and action configuration
// WHEN POST /api/v1/promotion-rules/{id}/actions
// THEN 200 with created action

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/{id}/actions
- Authentication: JWT Bearer
- Request: { action_type: string, discount_value?: number, reward_items?: array, attributes?: object }
- Response: 200 { code: "success", data: { id: number, rule_id: number, action_type: string, discount_value: number, reward_items: array, attributes: object } }

**Acceptance Criteria:**
- Validates ENUM values for action_type
- Validates discount_value range for DISCOUNT_PERCENT (0-100)
- Requires reward_items when action_type=FREE_ITEM

---

Task: [API][FAIL][REQUEST] - Promotion Action Request Errors
// GIVEN missing or invalid action data
// WHEN POST /api/v1/promotion-rules/{id}/actions
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/{id}/actions
- Authentication: JWT Bearer
- Request: { action_type?: string, discount_value?: number, reward_items?: array, attributes?: object }
- Response: 400 { code: "invalid_request", message: string }

**Acceptance Criteria:**
- 400 for missing action_type
- 400 for invalid ENUM value
- 400 for discount_value > 100 when type is DISCOUNT_PERCENT
- 400 for negative discount_value
- 400 for missing reward_items when FREE_ITEM

---

Task: [API][FAIL][RESOURCE] - Promotion Action Resource Errors
// GIVEN non-existent rule or action id
// WHEN operating on promotion actions
// THEN 400 with resource error

**API Specification:**
- Endpoint: GET/POST /api/v1/promotion-rules/{id}/actions
- Authentication: JWT Bearer
- Request: { params: { id: number } }
- Response: 400 { code: "resource_not_found", message: string }

**Acceptance Criteria:**
- 400 for non-existent rule id
