package ru.kolomiec.taskspring.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.dto.TaskDTO;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.entity.ToDoTime;
import ru.kolomiec.taskspring.repository.TaskRepository;
import ru.kolomiec.taskspring.security.jwt.JwtResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TaskRestControllerTest extends ControllerTestBase {
    @Autowired
    private TaskRepository taskRepository;
    @Test
    public void shouldReturnZeroTask()throws Exception {
        String responseContent = registerPerson(testPerson).getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(responseContent, JwtResponse.class);

        mockMvc.perform(get(BASE_URL + "/task/all-tasks")
                .header("Authorization", jwtResponse.getToken()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        Assertions.assertTrue(taskRepository.findAllByOwnerUsername(testPerson.getUsername()).get().isEmpty());
    }

    @Test
    public void shouldAddNewTask() throws Exception {
        String responseContent = registerPerson(testPerson).getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(responseContent, JwtResponse.class);
        TaskDTO testTask = new TaskDTO("do something", new ToDoTime());

        mockMvc.perform(post(BASE_URL + "/task/add-task")
                    .header("Authorization", jwtResponse.getToken())
                    .content(objectMapper.writeValueAsString(testTask))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        List<Task> testPersonTasks = taskRepository.findAllByOwnerUsername(testPerson.getUsername()).get();
        Assertions.assertEquals(1, testPersonTasks.size());
    }
}
