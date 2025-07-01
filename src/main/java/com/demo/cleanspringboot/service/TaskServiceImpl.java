package com.demo.cleanspringboot.service;

import com.demo.cleanspringboot.dto.task.TaskRequest;
import com.demo.cleanspringboot.dto.task.TaskResponse;
import com.demo.cleanspringboot.exception.DataNotFoundException;
import com.demo.cleanspringboot.model.TaskEntity;
import com.demo.cleanspringboot.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

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