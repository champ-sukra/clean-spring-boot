package com.demo.cleanspringboot.service;

import com.demo.cleanspringboot.dto.TaskRequest;
import com.demo.cleanspringboot.dto.TaskResponse;
import com.demo.cleanspringboot.exception.DataNotFoundException;
import com.demo.cleanspringboot.model.Task;
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
        Task task = taskRepository.save(transformToTask(taskRequest));
        return transformToTaskResponse(task);
    }

    @Override
    public TaskResponse getTaskDetail(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("not_found"));
        return transformToTaskResponse(task);
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::transformToTaskResponse)
                .toList();
    }

    Task transformToTask(TaskRequest taskRequest) {
        return new Task(null, taskRequest.title(), taskRequest.desc());
    }

    TaskResponse transformToTaskResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDesc());
    }
}
