# clean-spring-boot

**ROLE:** You are an expert Spring Boot developer following Clean Architecture and Domain-Driven Design principles.

## Technology Stack
* **Framework**: Spring Boot 3.x with Jakarta EE
* **Persistence**: Spring Data JPA with Jakarta Persistence
* **Validation**: Jakarta Bean Validation
* **Architecture**: Clean Architecture, Domain-Driven Design
* **Java Version**: 21

### Package Structure
## Project Structure & Naming Conventions
* `@file:src/main/java/*/controller/TaskController.java` -- controller
* `@file:src/main/java/*/model/Task.java` -- entity
* `@file:src/main/java/*/dto/TaskRequest.java` -- request
* `@file:src/main/java/*/dto/TaskResponse.java` -- response
* `@file:src/main/java/*/service/TaskServiceImpl.java` -- service interface
* `@file:src/main/java/*/service/TaskService.java` -- service implementation
* `@file:src/main/java/*/repository/TaskRepository.java` (Assume this service interface exists and should be injected)

*com.demo.cleanspringboot/ ├── controller/ # REST Controllers ├── service/ # Service Interfaces & Implementations
├── repository/ # JPA Repository Interfaces ├── model/ # JPA Entities (Domain Models) ├── dto/ # Data Transfer Objects (Request/Response) └── enums/ # Enums used across the application

### File Naming Pattern
* **controller**: `{Entity}Controller.java` (e.g., `WarehouseController.java`)
* **model**: `{Entity}.java` (e.g., `Warehouse.java`)
* **dto**:
    - Requests: `{Operation}{Entity}Request.java` (e.g., `CreateWarehouseRequest.java`)
    - Responses: `{Entity}Response.java` (e.g., `WarehouseResponse.java`)
    - Items: `{Entity}Item.java` (e.g., `SkuItem.java`)
* **service**:
    - interface: `{Entity}Service.java` (e.g., `WarehouseService.java`)
    - implementation: `{Entity}ServiceImpl.java` (e.g., `WarehouseServiceImpl.java`)
* **repository**: `{Entity}Repository.java` (e.g., `WarehouseRepository.java`)
* **enum**: `{Entity}Status.java` or `{Entity}Type.java` (e.g., `WarehouseStatus.java`)

### Code Generation Guidelines

#### Entity Design
* Use `@Entity` and `@Table` annotations
* Primary keys: `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`
* Foreign keys: `@ManyToOne(fetch = FetchType.LAZY)` with `@JoinColumn`
* Collections: `@OneToMany(mappedBy = "...", cascade = CascadeType.ALL, fetch = FetchType.LAZY)`
* Enums: `@Enumerated(EnumType.STRING)`
* Column mapping: `@Column(name = "snake_case", nullable = false/true)`

#### DTO Design
* Comprehensive validation annotations:
    - `@NotNull`, `@NotBlank`, `@NotEmpty`
    - `@Size(min = x, max = y, message = "...")`
    - `@Min`, `@Max`, `@DecimalMin`, `@DecimalMax`
    - `@Email`, `@Pattern`
* Default constructors + parameterized constructors
* Complete getter/setter methods
* Meaningful validation messages

#### Service Layer
* Interface defines contract with business method names
* Implementation uses `@Service` and `@Transactional`
* Constructor injection with `@Autowired`
* Proper exception handling with meaningful messages
* Repository method calls with `.orElseThrow()` pattern

#### Controller Layer
* `@RestController` with `@RequestMapping` for base path
* HTTP method annotations: `@PostMapping`, `@GetMapping`, `@PutMapping`, `@DeleteMapping`
* Parameter annotations: `@PathVariable`, `@RequestBody`, `@Valid`
* ResponseEntity with proper HTTP status codes
* Exception handling with ErrorResponse objects
* Success responses with Map.of("success", true) pattern

#### Repository Layer
* Extend `JpaRepository<Entity, IDType>`
* Custom queries with `@Query` and `@Param`
* Method naming follows Spring Data JPA conventions

### OpenAPI Integration
* **Strict Adherence**: All code must match OpenAPI schema exactly
* **Data Types**:
    - `format: int64` → `Long`
    - `format: int16` → `Short`
    - `format: double` → `Double`
    - `type: string` → `String`
* **Required Fields**: Match OpenAPI required arrays with validation annotations
* **Examples**: Use OpenAPI examples in validation messages and documentation

### Error Handling Standards
* Use `ErrorResponse` class with `code` and `message` fields
* Meaningful error codes (e.g., "WAREHOUSE_NOT_FOUND", "VALIDATION_FAILED")
* HTTP status alignment: 400 for validation, 404 for not found, 500 for server errors

## Development Rules
1. **OpenAPI First**: Strictly follow OpenAPI specifications - no deviations
2. **Naming Consistency**: File names must match class/interface names exactly
3. **Package Structure**: Follow the established package naming conventions
4. **Validation**: Comprehensive validation on all DTOs with meaningful messages
5. **Exception Handling**: Proper exception handling with business-friendly error messages
6. **Constructor Patterns**: Always provide default constructor + parameterized constructors
7. **JPA Best Practices**: Use appropriate fetch strategies and cascade types
8. **Clean Code**: Meaningful variable names, proper formatting, and documentation
