# [API] Promotion Actions - Create

Task: [API][SUCCESS] - Create Promotion Action
// GIVEN a valid rule id and action configuration
// WHEN POST /api/v1/promotion-rules/{id}/actions
// THEN 200 with created action

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/{id}/actions
- Authentication: JWT Bearer
- Request:
```json
{ action_type: string, discount_value?: number, reward_items?: array, attributes?: object 
```
- Response: 200
```json
{ code: "success", data: { id: number, rule_id: number, action_type: string, discount_value: number, reward_items: array, attributes: object 
``` }

**Acceptance Criteria:**
- Validates ENUM for action_type
- discount_value range for DISCOUNT_PERCENT (0-100)
- reward_items required when action_type=FREE_ITEM

---

Task: [API][FAIL][REQUEST] - Create Action Request Errors
// GIVEN invalid action payload
// WHEN POST /api/v1/promotion-rules/{id}/actions
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/{id}/actions
- Authentication: JWT Bearer
- Request:
```json
{ action_type?: string, discount_value?: number, reward_items?: array, attributes?: object 
```
- Response: 400
```json
{ code: "invalid_request", message: string 
```

**Acceptance Criteria:**
- 400 for missing/invalid action_type
- 400 for discount_value > 100 when DISCOUNT_PERCENT; negative values
- 400 for missing reward_items when FREE_ITEM

---

Task: [API][FAIL][RESOURCE] - Create Action Resource Errors
// GIVEN non-existent rule id
// WHEN POST /api/v1/promotion-rules/{id}/actions
// THEN 400 with resource not found

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules/{id}/actions
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

