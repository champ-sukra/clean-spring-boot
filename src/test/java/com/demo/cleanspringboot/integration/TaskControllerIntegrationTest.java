package com.demo.cleanspringboot.integration;

import com.demo.cleanspringboot.controller.TaskController;
import com.demo.cleanspringboot.dto.TaskRequest;
import com.demo.cleanspringboot.dto.TaskResponse;
import com.demo.cleanspringboot.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createTask_shouldPersistAndReturnTaskResponse() {
        TaskRequest taskRequest = new TaskRequest("title", "desc");

        ResponseEntity<TaskResponse> response = restTemplate.postForEntity("/tasks", taskRequest, TaskResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskRequest.title(), response.getBody().title());
    }

    @Test
    void createThenGetTask__shouldReturnTaskResponse() {
        TaskRequest taskRequest = new TaskRequest("title", "desc");

        //create the task
        ResponseEntity<TaskResponse> createResponse = restTemplate.postForEntity("/tasks", taskRequest, TaskResponse.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        Long id = createResponse.getBody().id();

        //get the task
        ResponseEntity<TaskResponse> getResponse = restTemplate.getForEntity("/tasks/" + id, TaskResponse.class);


        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(taskRequest.title(), getResponse.getBody().title());
    }
}
