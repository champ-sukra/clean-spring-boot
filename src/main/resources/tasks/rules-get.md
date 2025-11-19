# [API] Promotion Rules - Get By ID

Task: [API][SUCCESS] - Get Promotion Rule by ID

// GIVEN an existing rule id
// WHEN GET /api/v1/promotion-rules/{id}
// THEN 200 with rule details (conditions, actions, stacking)

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}
- Authentication: JWT Bearer
- Request:
```json
{
  "params": {
    "id": "number"
  }
}
```
- Response: 200
```json
{
  "code": "success",
  "data": {
    "id": "number",
    "template_id": "number",
    "rule_name": "string",
    "start_date": "string",
    "end_date": "string",
    "active": "boolean",
    "priority": "number",
    "quota": "object",
    "quota_used": "number",
    "conditions": "array",
    "actions": "array",
    "stacking": "object",
    "created_at": "string",
    "updated_at": "string"
  }
}
```

**Acceptance Criteria:**
- Includes conditions[], actions[], stacking

---

Task: [API][FAIL][RESOURCE] - Get Rule Resource Errors

// GIVEN non-existent rule id
// WHEN GET /api/v1/promotion-rules/{id}
// THEN 400 with resource not found

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules/{id}
- Authentication: JWT Bearer
- Request:
```json
{
  "params": {
    "id": "number"
  }
}
```
- Response: 400
```json
{
  "code": "resource_not_found",
  "message": "string"
}
```

**Acceptance Criteria:**
- 400 when id not found

