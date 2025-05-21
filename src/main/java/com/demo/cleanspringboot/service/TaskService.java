package com.demo.cleanspringboot.service;

import com.demo.cleanspringboot.dto.TaskRequest;
import com.demo.cleanspringboot.dto.TaskResponse;
import com.demo.cleanspringboot.exception.DataNotFoundException;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest taskRequest);
    TaskResponse getTaskDetail(Long id);
    List<TaskResponse> getAllTasks();
}
