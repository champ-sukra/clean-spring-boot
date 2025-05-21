package com.demo.cleanspringboot.service;

import com.demo.cleanspringboot.dto.TaskRequest;
import com.demo.cleanspringboot.dto.TaskResponse;
import com.demo.cleanspringboot.model.Task;
import com.demo.cleanspringboot.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskServiceImpl taskService;

    @Test
    void createTask_shouldReturnTaskResponse() {
        //given
        TaskRequest taskRequest = new TaskRequest("title", "desc");
        Task task = new Task(1L, "title", "desc");

        //when
        when(taskRepository.save(any())).thenReturn(task);

        //then
        TaskResponse taskResponse = taskService.createTask(taskRequest);

        assertEquals(taskRequest.title(), taskResponse.title());
        assertEquals(taskRequest.desc(), taskResponse.desc());
        assertNotNull(taskResponse.id());
    }
}
