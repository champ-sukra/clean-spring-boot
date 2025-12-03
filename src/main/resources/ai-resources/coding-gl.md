# 🤖 AI Coding Guideline – Spring Boot REST API with DDD + Hexagonal Architecture

> Target stack: **Spring Boot 3**, **Java 21**, **Maven/Gradle**, **REST API**, **DDD**, **Hexagonal (Ports & Adapters)**

---

## CRITICAL: AI Response Format

**NEVER provide summaries, explanations, or update notes in chat responses.**
- Do NOT say "Here's what was accomplished" or "Summary:"
- Do NOT list changes made
- Do NOT provide "Implementation Complete" sections
- ONLY respond with direct answers or confirmation when task is done
- Let the code and tool outputs speak for themselves

---

## 0. 📋 Reading Sequence Diagrams (CRITICAL)

### 0.1 Understanding Folder Paths from Sequence Diagrams

When implementing features, **ALWAYS refer to sequence diagrams first**. The participant definition contains critical information:

```puml
participant "PromotionRuleController\n(infrastructure/adapter/in/web)" as Controller
participant "PromotionRuleService\n(application/service)" as Service
participant "PromotionRuleRepository\n(infrastructure/adapter/out/persistence)" as Repository
```

**The second line (in parentheses) is the EXACT folder path where the class MUST be located.**

### 0.2 Folder Path Mapping

| Participant | Class Name | Folder Path (from project root) |
|------------|------------|--------------------------------|
| Controller | `PromotionRuleController` | `src/main/java/com/demo/cleanspringboot/infrastructure/adapter/in/web/` |
| Service | `PromotionRuleService` | `src/main/java/com/demo/cleanspringboot/application/service/` |
| Repository | `PromotionRuleRepository` | `src/main/java/com/demo/cleanspringboot/infrastructure/adapter/out/persistence/` |

### 0.3 Rules for Following Sequence Diagrams

1. **ALWAYS create/locate classes in the EXACT folder path specified**
   - The path shown in parentheses is MANDATORY
   - Do NOT move classes to different folders
   - Do NOT simplify the folder structure

2. **Follow the interaction flow exactly as shown**
   - If diagram shows: `Controller → Service → Repository`
   - Then implement exactly that: Controller calls Service, Service calls Repository
   - Do NOT skip layers or add extra layers not shown

3. **Use the method names shown in the diagram**
   - If diagram shows: `Service: getPromotionRuleDetail(ruleId)`
   - Then implement method with that exact name and signature

4. **Return types must match the diagram**
   - If diagram shows: `Repository --> Service: PromotionRuleEntity`
   - Then Repository must return `PromotionRuleEntity` type

5. **Transformation points are shown in the diagram**
   - If diagram shows: `Service -> Service: transformToPromotionRuleDTO(promotionRuleEntity)`
   - Then implement that transformation in the Service layer

### 0.4 Example Implementation from Diagram

Given this sequence:
```puml
Controller -> Service: getPromotionRuleDetail(ruleId)
Service -> Repository: findById(ruleId)
Repository --> Service: PromotionRuleEntity
Service -> Service: transformToPromotionRuleDTO(promotionRuleEntity)
Service --> Controller: PromotionRuleResponse
```

Implement as:

```java
// infrastructure/adapter/in/web/PromotionRuleController.java
@RestController
@RequestMapping("/api/v1/promotion-engine")
public class PromotionRuleController {
    private final PromotionRuleService service;
    
    @GetMapping("/{id}")
    public ResponseEntity<PromotionRuleResponse> getPromotionRuleDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPromotionRuleDetail(id));
    }
}

// application/service/PromotionRuleService.java
@Service
public class PromotionRuleService {
    private final PromotionRuleRepository repository;
    
    public PromotionRuleResponse getPromotionRuleDetail(Long ruleId) {
        PromotionRuleEntity entity = repository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("PromotionRule", ruleId));
        return transformToPromotionRuleDTO(entity);
    }
    
    private PromotionRuleResponse transformToPromotionRuleDTO(PromotionRuleEntity entity) {
        // transformation logic
    }
}

// infrastructure/adapter/out/persistence/PromotionRuleRepository.java
@Repository
public interface PromotionRuleRepository extends JpaRepository<PromotionRuleEntity, Long> {
}
```

### 0.5 Checklist Before Implementation

- [ ] Read the sequence diagram completely
- [ ] Identify all participants and their folder paths
- [ ] Note all method names and parameters
- [ ] Identify transformation points
- [ ] Note return types for each method
- [ ] Create classes in the EXACT folder paths shown
- [ ] Implement the EXACT flow shown in the diagram

**⚠️ CRITICAL: Sequence diagrams are the source of truth. Code structure must match them exactly.**

---

## 1. 📂 High-Level Project Structure

`com.example.project`

```plaintext
├── CleanApplication.java                                      # @SpringBootApplication

├── domain/                                                    # PURE domain (no Spring, no JPA)
│   ├── model/
│   │   └── {AggregateRoot}.java                               # Domain aggregates/entities (NO annotations)
│   ├── valueobject/
│   │   └── {ValueObject}.java                                 # Immutable domain VOs
│   ├── service/
│   │   └── {Aggregate}DomainService.java                      # Domain-level business rules
│   ├── event/
│   │   └── {DomainEvent}.java                                 # Pure domain events
│   └── exception/
│       └── {Domain}Exception.java                             # Domain-level exceptions

├── application/                                               # Application orchestration (use cases)
│   ├── port/
│   │   └── out/                                               # Outbound ports (external dependencies)
│   │       ├── {Aggregate}RepositoryPort.java                 # Persistence operations contract
│   │       └── {ExternalClient}Port.java                      # External API contracts (REST/Kafka/etc.)
│   ├── service/
│   │   └── {Aggregate}Service.java                            # Application service (orchestration only)
│   ├── dto/
│   │   ├── request/
│   │   │   ├── {Aggregate}CreateRequest.java
│   │   │   ├── {Aggregate}UpdateRequest.java
│   │   │   └── {Aggregate}SearchRequest.java                  # Optional
│   │   └── response/
│   │       └── {Aggregate}Response.java
│   └── mapper/
│       └── {Aggregate}DtoMapper.java                          # Maps domain ↔ DTO (application layer)

├── infrastructure/                                            # Infrastructure layer (framework concerns)
│   ├── adapter/
│   │   ├── in/
│   │   │   ├── web/
│   │   │   │   └── {Aggregate}Controller.java                 # REST controller (inbound adapter)
│   │   │   └── messaging/
│   │   │       └── {Event}Consumer.java                       # Kafka/SQS consumers (optional)
│   │   └── out/
│   │       ├── persistence/
│   │       │   ├── entity/
│   │       │   │   └── {Aggregate}Entity.java                 # JPA entity (infrastructure concern)
│   │       │   ├── repository/
│   │       │   │   └── Jpa{Aggregate}Repository.java          # Spring Data JPA repository
│   │       │   ├── mapper/
│   │       │   │   └── {Aggregate}EntityMapper.java           # Maps Entity ↔ Domain
│   │       │   └── {Aggregate}RepositoryAdapter.java          # Implements RepositoryPort
│   │       ├── rest/
│   │       │   └── {ExternalClient}Adapter.java               # External API client adapter
│   │       └── messaging/
│   │           └── {Event}PublisherAdapter.java               # Event publisher adapter
│   └── config/
│       ├── OpenApiConfig.java                                 # OpenAPI/Swagger configuration
│       ├── WebSecurityConfig.java                             # Security configuration
│       ├── DatabaseConfig.java                                # Flyway, DataSource, Hibernate
│       ├── FeignConfig.java                                   # REST clients (Feign/WebClient)
│       ├── KafkaConfig.java                                   # Kafka configuration
│       └── CacheConfig.java                                   # Redis/Cache configuration

└── common/                                                    # Shared cross-cutting concerns
    ├── exception/
    │   ├── GlobalExceptionHandler.java                        # Maps exceptions to HTTP responses
    │   ├── ErrorResponse.java                                 # Standard error response DTO
    │   └── ApplicationException.java                          # Base application exception
    └── util/
        └── DateTimeUtils.java                                 # Utility classes

```

---

## 2. 🏷 Naming Conventions

| Type                | Naming                                | Location                              |
| ------------------- |---------------------------------------|---------------------------------------|
| Domain Entity       | `Order`                               | `domain/model/`                       |
| JPA Entity          | `OrderEntity`                         | `infrastructure/adapter/out/persistence/entity/` |
| Repository Port     | `OrderRepositoryPort`                 | `application/port/out/`               |
| Repository Adapter  | `OrderRepositoryAdapter`              | `infrastructure/adapter/out/persistence/` |
| JPA Repository      | `JpaOrderRepository`                  | `infrastructure/adapter/out/persistence/repository/` |
| External Port       | `PaymentGatewayPort`, `EventPublisherPort` | `application/port/out/` |
| External Adapter    | `PaymentGatewayAdapter`, `SNSClientAdapter` | `infrastructure/adapter/out/rest/` or `messaging/` |
| Application Service | `OrderService`                        | `application/service/`                |
| Domain Service      | `OrderDomainService`                  | `domain/service/` (if complex)        |
| Controller          | `OrderController`                     | `infrastructure/adapter/in/web/`      |
| DTO                 | `OrderCreateRequest`, `OrderResponse` | `application/dto/request/` or `response/` |
| DTO Mapper          | `OrderDtoMapper`                      | `application/mapper/` (Domain ↔ DTO)  |
| Entity Mapper       | `OrderEntityMapper`                   | `infrastructure/adapter/out/persistence/mapper/` (Domain ↔ Entity) |

---

## 3. 🔁 Hexagonal Rules (Ports & Adapters)

### 3.1 Dependency Direction

```text
infrastructure → application → domain
```

* `domain` **does not depend** on `application` or `infrastructure`
* `application` **depends** on `domain`
* `infrastructure` **depends** on both `application` & `domain`

### 3.2 Allowed Responsibilities

| Layer       | Responsibilities                                         | Forbidden                                    |
| ----------- | -------------------------------------------------------- | -------------------------------------------- |
| Domain      | Entities, VOs, invariants, business rules, domain events | Frameworks, annotations, DTOs, HTTP, JPA     |
| Application | Use cases, transactions, orchestrating domain + ports    | Low-level persistence / HTTP / infra details |
| Adapter In  | Accept input (HTTP/message), map ↔ DTO, call use cases   | Business rules, DB logic                     |
| Adapter Out | Implement ports using JPA/REST/etc                       | Business decisions, domain rules             |

---

## 4. 🌐 REST & HTTP Guidelines

### 4.1 Resource Design

* Use **nouns** and plural:

    * `GET /api/v1/orders`
    * `GET /api/v1/orders/{id}`
    * `POST /api/v1/orders`
    * `PUT /api/v1/orders/{id}`
    * `DELETE /api/v1/orders/{id}`

### 4.2 HTTP Methods

* `GET` → read (idempotent, no side effects)
* `POST` → create or non-idempotent operation
* `PUT` → full update (idempotent)
* `PATCH` → partial update (non-idempotent)
* `DELETE` → remove (idempotent)

### 4.3 HTTP Status Codes

* `200 OK` → Successful GET/PUT/PATCH
* `201 Created` → Resource created (include `Location` header)
* `204 No Content` → Successful DELETE
* `400 Bad Request` → Validation errors
* `401 Unauthorized` → Not authenticated
* `403 Forbidden` → Authenticated but not allowed
* `404 Not Found` → Resource does not exist
* `409 Conflict` → Versioning / business conflicts
* `500+` → Unexpected server errors

---

## 5. 🧩 Port Design Rules

### 5.1 Application Services (No Inbound Ports)
Application services are **called directly** by inbound adapters (controllers, schedulers, consumers).

```java
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepositoryPort orderRepositoryPort;
    private final PaymentGatewayPort paymentGatewayPort;

    public OrderResponse createOrder(OrderCreateRequest request) {
        Order order = Order.create(request.toDomain());
        paymentGatewayPort.validatePayment(order);
        orderRepositoryPort.save(order);
        return OrderResponse.from(order);
    }
}
```

Controllers inject services directly:

```java
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    
    @PostMapping("/api/v1/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderCreateRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }
}
```

---

### 5.2 Outbound Ports (Application/Port/Out)
Define **dependencies the application needs** to perform its use cases.

Examples:
* Repository ports (for DB)
* Messaging ports (Kafka/SNS)
* REST client ports (external APIs)

```java
public interface PaymentGatewayPort {
    PaymentResult validatePayment(Order order);
}
```

Implemented by outbound adapters in `infrastructure/adapter/out`:

```java
@Component
@RequiredArgsConstructor
public class PaymentGatewayAdapter implements PaymentGatewayPort {

    private final WebClient webClient;

    @Override
    public PaymentResult validatePayment(Order order) {
        return webClient.post()
            .uri("https://external.api/payments/validate")
            .bodyValue(order)
            .retrieve()
            .bodyToMono(PaymentResult.class)
            .block();
    }
}
```

✅ All external system calls (DB, REST, SNS, etc.) go **through ports defined in `application/port/out`**.

---

## 6. ✔️ Core Coding Rules

```markdown
0. Sequence Diagrams (HIGHEST PRIORITY):
   - ALWAYS read sequence diagrams before implementing
   - Follow folder paths EXACTLY as specified in diagrams
   - Use method names EXACTLY as shown in diagrams
   - Follow the interaction flow EXACTLY as diagrammed
   - Sequence diagrams override all other guidelines if conflicts exist

1. Domain layer:
   - NO @Entity, @Table, @Service, @Component, @Autowired.
   - Only plain Java + business logic.

2. Ports:
   - Define external dependencies (`port/out`) as interfaces in the application layer.
   - No inbound ports - controllers call services directly.

3. DTOs:
   - Stay in application/infrastructure layers only.
   - Never leak DTOs into domain.

4. Controllers:
   - Only mapping, validation, and calling use cases.
   - Never talk to infrastructure adapters directly.

5. Repositories:
   - Domain uses repository *ports* (interfaces).
   - Infrastructure implements them with JPA, JDBC, or REST.

6. Mapping:
   - Use explicit mapper classes (`DtoMapper`, `EntityMapper`).
   - No MapStruct inside domain.

7. External API Calls:
   - Must go through `application/port/out` → implemented by `adapter/out/rest`.

8. Exceptions:
   - Domain throws domain-specific exceptions.
   - GlobalExceptionHandler maps them to HTTP status + ErrorResponse.

9. Validation:
   - Use `@Valid` + Bean Validation in DTOs.
   - Domain also enforces invariants.

10. Green Build (MANDATORY):
    - After EVERY code change, verify: ./gradlew build passes
    - All tests must be green
    - No compilation errors
    - Task is NOT complete until build is green ✅
```

---

## 7. 🧪 Testing Guidelines

### 7.1 Domain Tests (Unit)
* Test **domain services**, **aggregates**, **value objects**
* No Spring context, no DB

### 7.2 Application Tests
* Test use cases with **mocked ports**
* Verify orchestration (e.g., DB + external API call)

### 7.3 Integration Tests
* Use Spring Boot Test + Testcontainers
* Test full flow:  
  HTTP → Controller → Service → Out Port → Adapter → DB/API

### 7.4 Contract / API Tests
* Use RestAssured / WebTestClient
* Validate HTTP contract & schema

---

## 8. 🚫 Anti-Patterns (Do NOT)

```markdown
- Injecting adapters (JPA/REST clients) directly into controllers
- Calling external APIs directly from application/service
- Annotating domain models with @Entity / @Component
- Returning JPA entities from controllers
- Mixing DTOs into domain models
- Circular dependencies (e.g. infra → app → infra)
- Putting domain logic in controllers or adapters
```

---

## 9. ✅ Example Inbound + Outbound Flow (Order)

```text
POST /api/v1/orders
    ↓
OrderController (Adapter-In)
    ↓  CreateOrderRequest DTO
OrderService (Application/Service)
    ↓  uses domain + out ports
OrderRepositoryPort, PaymentGatewayPort (Application/Port/Out)
    ↓
OrderRepositoryAdapter (DB) + PaymentGatewayAdapter (REST)
    ↓
Database + External API
```

---

## 11. 🟢 Green Build Requirement (MANDATORY)

### 11.1 After Every Code Change
**EVERY code modification MUST result in a green build before considering the task complete.**

### 11.2 Verification Steps

After making ANY code changes, you MUST:

1. **Compile Check**
   ```bash
   ./gradlew clean build -x test
   # or for Maven
   ./mvnw clean compile -DskipTests
   ```
   ✅ **MUST pass** with no compilation errors

2. **Run All Tests**
   ```bash
   ./gradlew test
   # or for Maven
   ./mvnw test
   ```
   ✅ **MUST pass** with all tests green

3. **Check for Errors**
   - Use IDE error indicators (red underlines)
   - Check compilation output
   - Verify no missing imports
   - Ensure no type mismatches

### 11.3 Common Issues to Fix

| Issue | Solution |
|-------|----------|
| Missing imports | Add required import statements |
| Type mismatch | Fix return types, parameter types |
| Missing dependencies | Add to `build.gradle.kts` or `pom.xml` |
| Unused variables | Remove or use them |
| Missing methods | Implement required interface methods |
| Annotation errors | Add Spring Boot starters if needed |

### 11.4 Build Failure Response

If build fails:
1. **Read the error message carefully**
2. **Fix the root cause** (not just symptoms)
3. **Re-run the build**
4. **Repeat until green** ✅

### 11.5 Definition of "Done"

A task is **ONLY complete** when:
- ✅ Code compiles successfully
- ✅ All tests pass
- ✅ No compilation errors in IDE
- ✅ No breaking changes to existing code
- ✅ `./gradlew build` (or `./mvnw verify`) returns exit code 0

### 11.6 Integration Test Requirement

For endpoint implementations:
```bash
# Must run integration tests
./gradlew integrationTest
# or
./mvnw verify -P integration-tests
```

### 11.7 Pre-Commit Checklist

Before committing code:
- [ ] `./gradlew clean build` passes
- [ ] All tests green
- [ ] No compiler warnings (if strict mode)
- [ ] Code formatted (if formatter enabled)
- [ ] No TODO or FIXME left unaddressed

---

## 10. 🔚 Summary Principles

1. **Sequence diagrams are the source of truth** → Always implement EXACTLY what the diagram shows.
2. **Folder paths are mandatory** → Use the exact paths specified in diagram participants.  
3. **Business first** → Domain models express core business logic.  
4. **Framework as detail** → Spring, JPA, REST are replaceable adapters.  
5. **Ports define contracts** → Adapters fulfill them.  
6. **Orchestration lives in application/services** → Not in controllers or adapters.  
7. **Explicit mapping** → Domain, DTO, and Entity never cross boundaries.  
8. **External API calls always go through port/out** → never directly from inbound.  
9. **Test the core** → Domain and use cases are the highest-value tests.

---
