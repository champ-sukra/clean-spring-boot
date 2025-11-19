package com.demo.cleanspringboot.infrastructure.adapter.in.web;

import com.demo.cleanspringboot.application.dto.task.TaskRequest;
import com.demo.cleanspringboot.application.dto.task.TaskResponse;
import com.demo.cleanspringboot.application.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
        return new ResponseEntity<>(taskService.createTask(taskRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TaskResponse taskResponse = taskService.getTaskDetail(id);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }
}

//controller
//validate request body (DTO (Request Body))
//transform Entity (Database) -> DTO (Response Body)

//service
//transform DTO (request body) -> Entity