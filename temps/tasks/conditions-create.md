# [API] Promotion Conditions - Create

Task: [API][SUCCESS] - Create Promotion Condition
// GIVEN a valid rule id and condition configuration
// WHEN POST /api/v1/promotion-rules/{id}/conditions
// THEN 200 with created condition

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/{id}/conditions
- Authentication: JWT Bearer
- Request:
```json
{ condition_type: string, threshold_value?: number, include_product_ids?: array, include_category_ids?: array, exclude_product_ids?: array, attributes?: object 
```
- Response: 200
```json
{ code: "success", data: { id: number, rule_id: number, condition_type: string, threshold_value: number, include_product_ids: array, include_category_ids: array, exclude_product_ids: array, attributes: object 
``` }

**Acceptance Criteria:**
- Validates ENUM values for condition_type
- threshold_value >= 0 for QUANTITY/AMOUNT

---

Task: [API][FAIL][REQUEST] - Create Condition Request Errors
// GIVEN invalid payload
// WHEN POST /api/v1/promotion-rules/{id}/conditions
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/{id}/conditions
- Authentication: JWT Bearer
- Request:
```json
{ condition_type?: string, threshold_value?: number, include_product_ids?: array, include_category_ids?: array, exclude_product_ids?: array, attributes?: object 
```
- Response: 400
```json
{ code: "invalid_request", message: string 
```

**Acceptance Criteria:**
- 400 for missing/invalid condition_type
- 400 for negative threshold_value
- 400 for malformed JSON arrays

---

Task: [API][FAIL][RESOURCE] - Create Condition Resource Errors
// GIVEN non-existent rule id
// WHEN POST /api/v1/promotion-rules/{id}/conditions
// THEN 400 with resource not found

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/{id}/conditions
- Authentication: JWT Bearer
- Request:
```json
{ 
```
- Response: 400
```json
{ code: "resource_not_found", message: string 
```

**Acceptance Criteria:**
- 400 when rule id not found

