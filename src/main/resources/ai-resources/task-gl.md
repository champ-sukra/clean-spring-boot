# Coding Task Guideline (API-focused)

## Purpose
Generate concise, implementation-ready coding tasks from a given epic. Keep scope minimal and developer-only, focusing primarily on API endpoints. Include DB notes only when strictly required for the endpoint to function.

## EPIC and Story Completeness Verification
Before generating tasks, verify the EPIC and corresponding stories meet these requirements:

### Story-Epic Alignment Check:
**MANDATORY**: Verify all epic acceptance criteria have corresponding user stories:
- [ ] Each epic acceptance criteria section has matching stories in stories.md
- [ ] All epic features are represented by complete user stories
- [ ] No missing stories that are referenced in tasks.md
- [ ] Stories cover all epic business capabilities

**Action**: If stories are missing or incomplete compared to epic acceptance criteria, REJECT and require story completion before task generation.

### Task Completeness Verification:
**MANDATORY**: Before approving task generation, verify every task contains complete API specification:

#### Required Task Elements Check:
For EVERY task, verify all elements are present and complete:
- [ ] **Task Header**: `Task: [COMPONENT][SCENARIO] - Description`
- [ ] **BDD Comment**: `// GIVEN... // WHEN... // THEN...`
- [ ] **API Specification Section** with ALL four required fields:
  - [ ] **Endpoint**: `METHOD /complete/path` (NEVER missing)
  - [ ] **Authentication**: `None | JWT Bearer | Admin Required`
  - [ ] **Request**: `{ field: type }` format
  - [ ] **Response**: `status { code: "value", data/message: type }`
- [ ] **Acceptance Criteria Section**: Bulleted implementation requirements

#### Critical Verification Rules:
1. **Missing Endpoint Field**: IMMEDIATE REJECTION - every task MUST have complete Endpoint specification
2. **Inconsistent Schema Fields**: Verify all tasks for same endpoint use identical request/response field names
3. **Authentication Alignment**: Verify auth requirements match between related tasks
4. **Response Format Consistency**: All responses follow standard success/error structure

**Action**: If ANY task is missing required elements or has inconsistencies, REJECT and require complete task specification before approval.

### Story Standard Verification:
Each user story in the EPIC must follow this format:
```
As a <role>
I want <goal>
So that <business value>
```

### Acceptance Criteria (BDD Scenarios) Check:
```
✓ GIVEN (Preconditions)
  - Business context only (no technical details)
  - User roles/permissions in plain language
  - System state assumptions

✓ WHEN (Actions)
  - User actions in business terms
  - No endpoints, schemas, or technical implementation
  - Clear trigger events

✓ THEN (Expected Outcomes)
  - Business value delivered
  - Observable behavior in user terms
  - Success/failure in plain language

✓ Scenario Quality Rules
  - Valid scenarios use "valid" → no technical constraints
  - Invalid scenarios describe broken business rules in plain language
  - Constraints in plain language (e.g., "at least 8 characters", not regex)
  - Success = "valid input"
  - Failure = describe broken rule in business terms

✓ SUCCESS Scenario Coverage (CRITICAL)
  - EVERY user story MUST include at least ONE success/valid scenario
  - Success scenarios describe the primary happy path
  - Success scenarios come FIRST before failure scenarios
  - Missing success scenarios = INCOMPLETE STORY = IMMEDIATE REJECTION
```

### Story Verification Checklist:
- [ ] Uses proper story format (As a/I want/So that)
- [ ] **HAS AT LEAST ONE SUCCESS/VALID SCENARIO (MANDATORY)**
- [ ] **Success scenario appears BEFORE failure scenarios**
- [ ] Stories describe business behavior only
- [ ] No technical details (endpoints, schemas, regex)
- [ ] Constraints in plain language
- [ ] Valid scenarios use "valid input"
- [ ] Invalid scenarios describe broken business rules
- [ ] BDD format: Given/When/Then
- [ ] Automated BDD scenarios can be written
- [ ] Story is demonstrable to Product Owner

### Definition of Done Check:
- [ ] All acceptance criteria can be implemented
- [ ] Automated BDD scenarios can be written and tested
- [ ] Story can be demoed and accepted by Product Owner
- [ ] Business value is measurable

### Verification Questions:
1. **Business Focus**: Are stories written in business language without technical details?
2. **Story Format**: Does each story follow "As a/I want/So that" structure?
3. **Scenario Quality**: Do scenarios describe business rules, not technical constraints?
4. **Testability**: Can BDD scenarios be automated and verified?
5. **Demonstrability**: Can the story be demoed to show business value?

**Action**: If EPIC fails verification, REJECT and request revision to remove technical details and focus on business behavior before proceeding with task generation.

### Mandatory Rejection Criteria:
**STOP IMMEDIATELY and reject if any of these are found:**

0. **Missing SUCCESS Scenarios (CRITICAL)**:
   - **ANY user story without at least ONE success/valid scenario**
   - Success scenarios appearing AFTER failure scenarios
   - Stories with only failure/error scenarios
   - **Example violation**: Story about "User Login" that only has "Invalid Credentials", "Empty Fields", "Disabled Account" scenarios but NO "Valid Login" scenario
   - **This is the PRIMARY reason to reject stories** - every feature MUST show the happy path first

1. **Story-Epic Misalignment**:
   - Epic acceptance criteria missing corresponding user stories
   - Tasks reference stories that don't exist in stories.md
   - Epic features not covered by user stories
   - Incomplete story coverage of epic scope

1.5. **Task Specification Incompleteness**:
   - ANY task missing the **Endpoint** field in API Specification
   - Inconsistent request/response schemas across related tasks
   - Missing required API Specification elements (Endpoint, Authentication, Request, Response)
   - Malformed or incomplete task structure

2. **Non-Functional Requirements Present**:
   - Performance specifications (response times, throughput, etc.)
   - Infrastructure requirements (servers, databases, scaling)
   - Technology stack specifications (frameworks, libraries)
   - Monitoring/logging requirements
   - Security implementation details (encryption algorithms, token types)

3. **Missing Story Pattern Elements**:
   - Stories not in "As a/I want/So that" format
   - Missing Given/When/Then scenarios
   - No Definition of Done section
   - Technical implementation details in acceptance criteria

4. **Invalid Story Content**:
   - API endpoints mentioned in acceptance criteria
   - Database schema requirements
   - Code-level specifications
   - Framework or library references
   - Technical error codes or HTTP status codes

**Rejection Response Template**:
```
EPIC REJECTED: [Specific reason]

Issues found:
- [List specific violations]

Required actions:
- **ADD MISSING SUCCESS SCENARIOS to all stories (CRITICAL - must be first scenario)**
- Complete missing user stories to match epic acceptance criteria
- Fix all missing or incomplete task API specifications (especially missing Endpoint fields)
- Standardize inconsistent request/response schemas across related tasks
- Remove all non-functional requirements
- Convert technical details to business language
- Ensure all stories follow "As a/I want/So that" format
- Add proper Given/When/Then scenarios
- Include Definition of Done for each story
- Verify stories.md contains all stories referenced in tasks.md

Please revise the EPIC and complete all missing stories before requesting task generation.
```

## Task Naming Convention
Use: `[COMPONENT][SCENARIO] - Description`

Components:
- `[API]` — Backend API endpoints (primary)
- `[DB]` — Database changes directly required by the API (only if necessary)

Scenarios:
- `[SUCCESS]` — Happy path
- `[FAIL]` — Errors/validation

## Task Structure (BDD)
Each task follows this format for OpenAPI compatibility:

```
Task: [COMPONENT][SCENARIO] - Brief Description

// GIVEN (preconditions)
// WHEN (action)
// THEN (expected result)

**API Specification:**
- Endpoint: METHOD /path
- Authentication: JWT Bearer | None | Admin Required
- Request: { field: type, field: type }
- Response: status { code: "success", data: { field: type } } | status { code: "error_code", message: string }

**Acceptance Criteria:**
- Concrete, testable outcomes
- HTTP status codes and response shapes
- Constraints/validation rules
```

## OpenAPI Generation Requirements
Tasks must include these elements for ./open-api-guideline.md:

### Required API Elements:
- **Endpoint**: Complete path + HTTP method (e.g., POST /auth/login)
- **Authentication**: Mechanism type (JWT Bearer, None, Admin Required)
- **Request Schema**: Complete field definitions with types
- **Response Schema**: All possible responses with status codes and field types
- **Parameters**: Path/query parameters with types and requirements

### Schema Type Definitions:
- **string** - text data
- **number** - numeric values
- **boolean** - true/false
- **array** - list of items
- **object** - nested structure
- **file** - file upload (specify allowed types)

Notes:
- Do not include QA sections, performance plans, or infra items.
- Prefer referencing existing OpenAPI/auth-service.yaml where applicable.

## Status Code Policy
Only use 200, 400, 500 in all tasks:
- 200 for all successful outcomes (including creations, deletions, idempotent operations).
- 400 for any client-side error such as unauthorized, forbidden, not found, validation, conflicts, rate limits, or malformed input.
- 500 for unexpected server-side errors.
Adjust task wording accordingly (e.g., 'unauthorized' → 400).

## Response Format Standards
All API responses must follow these structures:

**Success Responses:**
```json
{
  "code": "success",
  "data": {
    // actual response data
  }
}
```

**Error Responses:**
```json
{
  "code": "business_error_code",
  "message": "Human-readable error description"
}
```
Example: `{ "code": "duplicate_record", "message": "Email already exists" }`

Common error codes:
- invalid_credentials, invalid_request, unauthorized_access
- account_disabled, account_expired, account_protected
- validation_error, resource_not_found, duplicate_resource

## Generation Rules
For each user story in the epic, generate coding tasks limited to API work:

1. **API Success task(s) [API][SUCCESS]**
   - Define URL, method, required auth, request/response schema, key side effects.

2. **API Failure/validation task(s) [API][FAIL] - Domain-Based Approach**
   - Split failure scenarios by **error domain** rather than lumping all errors together
   - Create separate tasks for each logical error domain
   - Each domain task should focus on related error conditions

3. **DB task(s) [DB]** only if the user story cannot be completed without schema/index changes.

Keep each task short (5–12 lines total). Focus on what must be coded.

## Error Domain Classification
Split [API][FAIL] tasks by these AUTH-related domains (NOUNS only):

### **REQUEST Domain**
- **Error Codes**: `invalid_request`
- **Focus**: Request format, required fields, data type validation
- **Task Name**: `[API][FAIL][REQUEST] - Request Validation Errors`

### **CREDENTIALS Domain**
- **Error Codes**: `invalid_credentials`, `unauthorized_access`
- **Focus**: Username/password verification, token validation
- **Task Name**: `[API][FAIL][CREDENTIALS] - Credential Verification Errors`

### **ACCOUNT Domain**
- **Error Codes**: `account_disabled`, `account_expired`, `account_protected`
- **Focus**: User account state, status-based restrictions
- **Task Name**: `[API][FAIL][ACCOUNT] - Account State Errors`

### **PROFILE Domain**
- **Error Codes**: `duplicate_resource`, `resource_not_found`, `resource_in_use`
- **Focus**: Profile data constraints, user profile management
- **Task Name**: `[API][FAIL][PROFILE] - Profile Data Errors`

## Minimal Example - Domain-Based Tasks
```
=== USER STORY: User Login ===

Task: [API][SUCCESS] - User Login
// GIVEN a registered user with valid credentials
// WHEN POST /auth/login
// THEN 200 with JWT response

**API Specification:**
- Endpoint: POST /auth/login
- Authentication: None
- Request: { identifier: string, password: string }
- Response: 200 { code: "success", data: LoginResponse }

**Acceptance Criteria:**
- Accepts username or email as identifier
- Issues signed JWT with expiration
- Response excludes sensitive fields

---

Task: [API][FAIL][REQUEST] - Login Request Errors
// GIVEN missing or malformed request data
// WHEN POST /auth/login
// THEN 400 with validation error response

**API Specification:**
- Endpoint: POST /auth/login
- Authentication: None
- Request: { identifier: string, password: string }
- Response: 400 { code: "invalid_request", message: string }

**Acceptance Criteria:**
- 400 for missing required fields (identifier, password)
- 400 for malformed JSON or invalid data types

---

Task: [API][FAIL][CREDENTIALS] - Login Credential Errors
// GIVEN incorrect credentials
// WHEN POST /auth/login
// THEN 400 with authentication error response

**API Specification:**
- Endpoint: POST /auth/login
- Authentication: None
- Request: { identifier: string, password: string }
- Response: 400 { code: "invalid_credentials", message: string }

**Acceptance Criteria:**
- 400 for wrong username/password combination
- Generic message prevents user enumeration

---

Task: [API][FAIL][ACCOUNT] - Login Account State Errors
// GIVEN user account with restricted state
// WHEN POST /auth/login
// THEN 400 with account state error response

**API Specification:**
- Endpoint: POST /auth/login
- Authentication: None
- Request: { identifier: string, password: string }
- Response: 400 { code: "account_disabled" | "account_expired" | "account_protected", message: string }

**Acceptance Criteria:**
- 400 for disabled accounts
- 400 for expired accounts
- 400 for protected accounts (security measures)

```
