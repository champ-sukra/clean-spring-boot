package com.demo.cleanspringboot.application.service;

import com.demo.cleanspringboot.application.dto.task.TaskRequest;
import com.demo.cleanspringboot.application.dto.task.TaskResponse;
import com.demo.cleanspringboot.domain.exception.DataNotFoundException;
import com.demo.cleanspringboot.domain.model.TaskEntity;
import com.demo.cleanspringboot.infrastructure.adapter.out.persistence.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(TaskRequest taskRequest) {
        TaskEntity taskEntity = taskRepository.save(transformToTask(taskRequest));
        return transformToTaskResponse(taskEntity);
    }

    public TaskResponse getTaskDetail(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("not_found"));
        return transformToTaskResponse(taskEntity);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::transformToTaskResponse)
                .toList();
    }

    TaskEntity transformToTask(TaskRequest taskRequest) {
        return new TaskEntity(null, taskRequest.title(), taskRequest.desc());
    }

    TaskResponse transformToTaskResponse(TaskEntity taskEntity) {
        return new TaskResponse(taskEntity.getId(), taskEntity.getTitle(), taskEntity.getDescription());
    }
}