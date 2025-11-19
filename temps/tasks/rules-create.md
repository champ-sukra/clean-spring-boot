# [API] Promotion Rules - Create

Task: [API][SUCCESS] - Create Promotion Rule

// GIVEN a valid template_id and configuration
// WHEN POST /api/v1/promotion-rules
// THEN 200 with created rule

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules
- Authentication: JWT Bearer
- Request:
```json
{
  "template_id": "number",
  "rule_name": "string",
  "start_date": "string",
  "end_date": "string",
  "active": "boolean (optional)",
  "priority": "number (optional)",
  "quota": "object (optional)"
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
    "created_at": "string",
    "updated_at": "string"
  }
}
```

**Acceptance Criteria:**
- Validates template_id exists
- Initializes quota_used=0; sets timestamps
- end_date > start_date

---

Task: [API][FAIL][REQUEST] - Create Rule Request Errors

// GIVEN invalid create payload
// WHEN POST /api/v1/promotion-rules
// THEN 400 with validation error

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules
- Authentication: JWT Bearer
- Request:
```json
{
  "template_id": "number (optional)",
  "rule_name": "string (optional)",
  "start_date": "string (optional)",
  "end_date": "string (optional)",
  "priority": "number (optional)",
  "quota": "object (optional)"
}
```
- Response: 400
```json
{
  "code": "invalid_request",
  "message": "string"
}
```

**Acceptance Criteria:**
- 400 for missing required fields
- 400 for invalid dates or malformed quota JSON

---

Task: [API][FAIL][RESOURCE] - Create Rule Resource Errors

// GIVEN invalid resource references or duplicates
// WHEN POST /api/v1/promotion-rules
// THEN 400 with resource error

**API Specification:**
- Endpoint: POST /api/v1/promotion-rules
- Authentication: JWT Bearer
- Request:
```json
{
  "template_id": "number",
  "rule_name": "string",
  "start_date": "string",
  "end_date": "string"
}
```
- Response: 400
```json
{
  "code": "resource_not_found | duplicate_resource",
  "message": "string"
}
```

**Acceptance Criteria:**
- 400 when template_id not found
- 400 when duplicate rule_name within same template

