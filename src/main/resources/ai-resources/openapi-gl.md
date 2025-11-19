# **AI OpenAPI Generation Directive (Minimal Mode)**

## **Primary Data Sources**

1. **Task (required)** — must exist before OpenAPI generation and must define the contract.
2. **Cap-Epic (supporting source)** — used for capability scope and confirmation.

**Do not extract technical details from Epics or Stories.**
**Do not generate OpenAPI unless a valid Task is provided.**

---

## **Task Dependency Requirement**

OpenAPI must only be generated **after** a Task is created and contains complete request/response specification.

If Task is missing or incomplete:

```
OPENAPI GENERATION BLOCKED
Reason: Task not found or incomplete
```

---

## **Generation Rules**

1. Use **OpenAPI 3.x YAML** format.

2. Endpoint must include: path, method, authentication, request, response, and error schemas.

3. All schemas must be defined under `components/schemas`.

4. No inline schema definitions inside `paths`.

5. Use `$ref` for all request and response bodies.

6. Field naming must use **camelCase**.

7. Schema naming must use **PascalCase**.

8. Wrap all responses using:

   ```
   { code: string, data: <object> }
   ```

9. Errors must use:

   ```
   { code: string, message: string }
   ```

10. Allowed HTTP status codes: **200, 400, 500** only.

---

## **Validation Rules**

If any required detail is missing, stop and return:

```
OPENAPI GENERATION BLOCKED
Missing: <item>
```

Required items:

* Endpoint + HTTP method
* Request schema fields + types
* Response schema fields + types
* Authentication type (JWT / None)
* Error codes list

---

## **Success Response Format**

```yaml
200:
  content:
    application/json:
      schema:
        type: object
        properties:
          code:
            type: string
            example: success
          data:
            $ref: '#/components/schemas/<SchemaName>'
```

---

## **Error Response Format**

```yaml
400:
  content:
    application/json:
      schema:
        $ref: '#/components/schemas/ErrorResponse'
```

---

## **ErrorResponse Schema**

```yaml
ErrorResponse:
  type: object
  properties:
    code:
      type: string
    message:
      type: string
```

---

## **File Locations**

```
/resources/task/*.md                        # required contract input
/resources/cap-epics/*.md                   # capability reference
/resources/openapi/<service>.yaml           # root OpenAPI file
/resources/openapi/components/*.yaml        # component schema files
```

---

## **Output File Structure**

**Primary output**

```
/resources/openapi/<service>.yaml
```

**Split component files**

```
/resources/openapi/components/
  <SchemaName>.yaml
  ErrorResponse.yaml
```

**Example final layout**

```
resources/
  task/
    login-api-task.md
  cap-epics/
    auth-cap-epic.md
  openapi/
    auth-service.yaml
    components/
      LoginRequest.yaml
      LoginResponse.yaml
      User.yaml
      ErrorResponse.yaml
```

---

## **Output Contract**

AI must wrap response as:

```
---BEGIN OPENAPI---
<yaml here>
---END OPENAPI---
```

If incomplete:

```
OPENAPI GENERATION BLOCKED
Missing: <item>
```
