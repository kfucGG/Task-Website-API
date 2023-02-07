package ru.kolomiec.taskspring.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.dto.TaskDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(value = "classpath:/sqlQuery/drop-task-table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sqlQuery/create-task-table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sqlQuery/drop-person-table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sqlQuery/create-person-table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TaskControllerITest extends ControllerIntegrationTestBase {

    private final PersonRegistrationDTO testPerson = new PersonRegistrationDTO("username", "password");

    private final TaskDTO testTask = new TaskDTO("test-task", null);
    private final String TASK_CONTROLLER_URL = "/api/task";
    @Test
    public void shouldReturnZeroTask() throws Exception {
        String token = makeRequestForRegistrationAndReturnToken(testPerson);
        mockMvc.perform(get(TASK_CONTROLLER_URL + "/all-tasks")
                    .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnTask() throws Exception {
        String token = makeRequestForRegistrationAndReturnToken(testPerson);
        makeRequestForSaveNewTask(testTask, token);
        mockMvc.perform(get(TASK_CONTROLLER_URL + "/all-tasks")
                    .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddNewTaskToPerson() throws Exception {
        String token = makeRequestForRegistrationAndReturnToken(testPerson);
        mockMvc.perform(post(TASK_CONTROLLER_URL + "/add-task")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(testTask))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeletePersonTask() throws Exception {
        String token = makeRequestForRegistrationAndReturnToken(testPerson);
        makeRequestForSaveNewTask(testTask, token);
        mockMvc.perform(delete(TASK_CONTROLLER_URL + "/1/delete-task")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotDeleteDefunctPersonTask() throws Exception {
        String token = makeRequestForRegistrationAndReturnToken(testPerson);
        mockMvc.perform(delete(TASK_CONTROLLER_URL + "/1/delete-task")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private void makeRequestForSaveNewTask(TaskDTO task, String authToken) throws Exception {
        mockMvc.perform(post(TASK_CONTROLLER_URL + "/add-task")
                .header("Authorization", authToken)
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON));
    }
}
