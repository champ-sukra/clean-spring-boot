# [API] Promotion Rules - List

Task: [API][SUCCESS] - List Promotion Rules

// GIVEN valid optional filter parameters
// WHEN GET /api/v1/promotion-rules
// THEN 200 with filtered, paginated list of rules

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules?status=number&templateId=number&page=number&size=number
- Authentication: JWT Bearer
- Query Parameters:
  - status: integer (optional) enum [1=PENDING,2=ACTIVE,3=EXPIRED]
  - templateId: integer (optional)
  - page: integer (optional, default=1, minimum=1)
  - size: integer (optional, default=20, minimum=1, maximum=100)
- Response: 200
```json
{
  "code": "success",
  "data": {
    "items": [
      {
        "id": 10,
        "template_id": 5,
        "rule_name": "Flash Sale",
        "start_date": "2025-01-01T00:00:00Z",
        "end_date": "2025-01-07T23:59:59Z",
        "status": 2,
        "priority": 0,
        "quota": { "type": "LIMIT", "value": 100 },
        "quota_used": 12,
        "created_at": "2025-01-01T00:00:00Z",
        "updated_at": "2025-01-02T00:00:00Z"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 20
  }
}
```

**Acceptance Criteria:**
- Filters: status (1=PENDING,2=ACTIVE,3=EXPIRED), templateId
- Pagination defaults: page=1, size=20
- Items include rule summary fields (no conditions/actions/stacking)
- Response envelope contains code + data.items + data.total + data.page + data.size

---

Task: [API][FAIL][REQUEST] - List Rules Request Errors

// GIVEN invalid filter parameters
// WHEN GET /api/v1/promotion-rules
// THEN 400 with validation error

**API Specification:**
- Endpoint: GET /api/v1/promotion-rules
- Authentication: JWT Bearer
- Possible Invalid Cases:
  - status not in [1,2,3]
  - templateId not numeric
  - page/size not numeric
  - page < 1 or size < 1 or size > 100
- Response: 400
```json
{
  "code": "invalid_request",
  "message": "Invalid status value"
}
```
Another example:
```json
{
  "code": "invalid_request",
  "message": "Invalid page value"
}
```

**Acceptance Criteria:**
- 400 for invalid status value
- 400 for non-numeric templateId/page/size
- 400 for out-of-range page/size

---

**Notes:**
- Status stored as integer; mapping documented above.
- Extend later with additional filters (e.g., priority) without breaking envelope.
