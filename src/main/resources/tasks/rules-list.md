# [API] Promotion Rules - List

Task: [API][SUCCESS] - List Promotion Rules

// GIVEN valid optional filter parameters
// WHEN GET /api/v1/promotion-rules
// THEN 200 with filtered, paginated list of rules

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules?status=string&templateId=number&active=boolean&page=number&size=number
- Authentication: JWT Bearer
- Request:
```json
{
  "query": {
    "status": "string (optional)",
    "templateId": "number (optional)",
    "active": "boolean (optional)",
    "page": "number (optional)",
    "size": "number (optional)"
  }
}
```
- Response: 200
```json
{
  "code": "success",
  "data": {
    "items": "array",
    "total": "number",
    "page": "number",
    "size": "number"
  }
}
```

**Acceptance Criteria:**
- Filters: status (ACTIVE|INACTIVE|EXPIRED), templateId, active
- Pagination defaults: page=1, size=20
- Items include rule summary fields

---

Task: [API][FAIL][REQUEST] - List Rules Request Errors

// GIVEN invalid filter parameters
// WHEN GET /api/v1/promotion-rules
// THEN 400 with validation error

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules
- Authentication: JWT Bearer
- Request:
```json
{
  "query": {
    "status": "string (optional)",
    "templateId": "number (optional)",
    "active": "boolean (optional)",
    "page": "number (optional)",
    "size": "number (optional)"
  }
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
- 400 for invalid status value
- 400 for non-numeric templateId/page/size

