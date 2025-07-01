package com.demo.cleanspringboot.service;

import com.demo.cleanspringboot.dto.task.TaskRequest;
import com.demo.cleanspringboot.dto.task.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest taskRequest);
    TaskResponse getTaskDetail(Long id);
    List<TaskResponse> getAllTasks();
}
