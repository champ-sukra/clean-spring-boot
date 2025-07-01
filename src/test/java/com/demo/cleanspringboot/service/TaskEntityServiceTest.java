package com.demo.cleanspringboot.service;

import com.demo.cleanspringboot.dto.task.TaskRequest;
import com.demo.cleanspringboot.dto.task.TaskResponse;
import com.demo.cleanspringboot.model.TaskEntity;
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
public class TaskEntityServiceTest {
    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskServiceImpl taskService;

    @Test
    void createTask_shouldReturnTaskResponse() {
        //given
        TaskRequest taskRequest = new TaskRequest("title", "desc");
        TaskEntity taskEntity = new TaskEntity(1L, "title", "desc");

        //when
        when(taskRepository.save(any())).thenReturn(taskEntity);

        //then
        TaskResponse taskResponse = taskService.createTask(taskRequest);

        assertEquals(taskRequest.title(), taskResponse.title());
        assertEquals(taskRequest.desc(), taskResponse.desc());
        assertNotNull(taskResponse.id());
    }
}
