# Promotion Conditions - Tasks (API Only)

Task: [API][SUCCESS] - List Promotion Conditions
// GIVEN a valid rule id
// WHEN GET /api/v1/promotion-rules/{id}/conditions
// THEN 200 with list of conditions

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}/conditions
- Authentication: JWT Bearer
- Request: { params: { id: number } }
- Response: 200 { code: "success", data: { items: array } }

**Acceptance Criteria:**
- Returns array of conditions for rule id
- Items include: id, rule_id, condition_type, threshold_value, include_product_ids, include_category_ids, exclude_product_ids, attributes

---

Task: [API][SUCCESS] - Create Promotion Condition
// GIVEN a valid rule id and condition configuration
// WHEN POST /api/v1/promotion-rules/{id}/conditions
// THEN 200 with created condition

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/{id}/conditions
- Authentication: JWT Bearer
- Request: { condition_type: string, threshold_value?: number, include_product_ids?: array, include_category_ids?: array, exclude_product_ids?: array, attributes?: object }
- Response: 200 { code: "success", data: { id: number, rule_id: number, condition_type: string, threshold_value: number, include_product_ids: array, include_category_ids: array, exclude_product_ids: array, attributes: object } }

**Acceptance Criteria:**
- Validates ENUM values for condition_type
- Validates threshold_value >= 0 for QUANTITY/AMOUNT
- Stores arrays and attributes as JSON

---

Task: [API][SUCCESS] - Delete Promotion Condition
// GIVEN a valid condition id
// WHEN DELETE /api/v1/promotion-conditions/{conditionId}
// THEN 200 with deletion confirmation

**API Specification:**
- Endpoint: DELETE /api/v1/promotion-conditions/{conditionId}
- Authentication: JWT Bearer
- Request: { params: { conditionId: number } }
- Response: 200 { code: "success", data: { id: number, deleted: boolean } }

**Acceptance Criteria:**
- Only deletes the condition entry
- Rule remains unchanged

---

Task: [API][FAIL][REQUEST] - Promotion Condition Request Errors
// GIVEN missing or invalid condition data
// WHEN POST /api/v1/promotion-rules/{id}/conditions
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/{id}/conditions
- Authentication: JWT Bearer
- Request: { condition_type?: string, threshold_value?: number, include_product_ids?: array, include_category_ids?: array, exclude_product_ids?: array, attributes?: object }
- Response: 400 { code: "invalid_request", message: string }

**Acceptance Criteria:**
- 400 for missing condition_type
- 400 for invalid ENUM value
- 400 for negative threshold_value when required
- 400 for malformed JSON arrays or attributes

---

Task: [API][FAIL][RESOURCE] - Promotion Condition Resource Errors
// GIVEN non-existent rule or condition
// WHEN operating on promotion conditions
// THEN 400 with resource error

**API Specification:**
- Endpoint: GET/POST /api/v1/promotion-rules/{id}/conditions | DELETE /api/v1/promotion-conditions/{conditionId}
- Authentication: JWT Bearer
- Request: { params: { id?: number, conditionId?: number } }
- Response: 400 { code: "resource_not_found", message: string }

**Acceptance Criteria:**
- 400 for non-existent rule id
- 400 for non-existent condition id
