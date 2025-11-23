# Simplified Coding Guideline – Spring Boot REST API (Controller → Service → Repository)

Goal: Keep things straightforward. Each feature follows a clear flow:
Controller (HTTP layer) → Service (business orchestration) → Repository (data access)

---
## 1. Project Layer Responsibilities (Simplified)

1. Controller layer (`controller/`)
   - Defines REST endpoints (@RestController)
   - Validates path/body parameters (basic checks)
   - Delegates to Service
   - Maps service responses to HTTP responses

2. Service layer (`service/`)
   - Implements business use cases
   - Transforms Request DTO -> Entity and Entity -> Response DTO
   - Calls Repository interfaces
   - Throws domain/application exceptions when needed

3. Repository layer (`repository/`)
   - Extends Spring Data JPA interfaces (e.g., JpaRepository)
   - Provides CRUD access to persistence
   - No business logic

4. Model & DTOs
   - Entity (`model/EntityNameEntity.java`): annotated with JPA annotations (@Entity)
   - Request DTO (`dto/...Request.java`): immutable, records or simple classes
   - Response DTO (`dto/...Response.java`): returned to clients

---
## 2. Naming Conventions

| Type          | Example                  |
|---------------|--------------------------|
| Controller    | TaskController           |
| Service (interface) | TaskService       |
| Service (impl) | TaskServiceImpl        |
| Repository    | TaskRepository          |
| Entity        | TaskEntity              |
| Request DTO   | TaskRequest             |
| Response DTO  | TaskResponse            |
| Exception     | DataNotFoundException   |

---
## 3. Typical Flow (Request Lifecycle)

GET /tasks/{id}
→ TaskController.getTaskById(id)
→ TaskService.getTaskDetail(id)
→ TaskRepository.findById(id)
→ TaskService maps Entity -> TaskResponse
→ Controller returns ResponseEntity(TaskResponse)

---
## 4. Error Handling (Simple)
- Throw `DataNotFoundException` when repository `.findById()` returns empty
- Use a single GlobalExceptionHandler (already present) to map exceptions

---
## 5. Task Module – Example Implementation

Below is the existing Task module demonstrating the pattern.

Controller snippet (`TaskController`):
```java
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) { this.taskService = taskService; }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
        return new ResponseEntity<>(taskService.createTask(taskRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") Long id) {
        if (id == null || id <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(taskService.getTaskDetail(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }
}
```

Service interface (`TaskService`):
```java
public interface TaskService {
    TaskResponse createTask(TaskRequest taskRequest);
    TaskResponse getTaskDetail(Long id);
    List<TaskResponse> getAllTasks();
}
```

Service implementation (`TaskServiceImpl`):
```java
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    public TaskServiceImpl(TaskRepository taskRepository) { this.taskRepository = taskRepository; }

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        TaskEntity taskEntity = taskRepository.save(transformToTask(taskRequest));
        return transformToTaskResponse(taskEntity);
    }

    @Override
    public TaskResponse getTaskDetail(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("not_found"));
        return transformToTaskResponse(taskEntity);
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream().map(this::transformToTaskResponse).toList();
    }

    TaskEntity transformToTask(TaskRequest taskRequest) {
        return new TaskEntity(null, taskRequest.title(), taskRequest.desc());
    }

    TaskResponse transformToTaskResponse(TaskEntity taskEntity) {
        return new TaskResponse(taskEntity.getId(), taskEntity.getTitle(), taskEntity.getDescription());
    }
}
```

Repository (`TaskRepository`):
```java
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {}
```

Request DTO (`TaskRequest` example):
```java
public record TaskRequest(String title, String desc) {}
```

Response DTO (`TaskResponse` example):
```java
public record TaskResponse(Long id, String title, String description) {}
```

Entity (`TaskEntity` example):
```java
@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    // constructors, getters
}
```

---
## 6. Implementation Checklist (Feature Template)

When adding a new feature (e.g., PromotionRule):
1. Create Entity (database table mapping)
2. Create Repository interface
3. Create Request & Response DTOs
4. Create Service interface + implementation
5. Create Controller with endpoints
6. Add basic validation (null/id checks)
7. Handle not-found with custom exception
8. Test manually with curl/Postman

---
## 7. Curl Testing Examples
```bash
# Create a task
curl -X POST http://localhost:8080/tasks \
  -H 'Content-Type: application/json' \
  -d '{"title":"Sample","desc":"Demo task"}'

# Get task detail
curl http://localhost:8080/tasks/1

# List tasks
curl http://localhost:8080/tasks
```

---
## 8. Guiding Principles (Simplified)
- Keep layers thin and focused
- No business logic inside controllers or repositories
- Service is the central place for orchestration
- DTOs isolate external contracts from internal entities
- Prefer records for simple immutable DTOs
- Fail fast on invalid input
- Keep naming consistent across modules

---
## 9. Definition of Done
- Feature endpoints respond with expected shapes
- No 500 errors on happy path
- Basic validation in place
- Repository calls work against the configured database

---

