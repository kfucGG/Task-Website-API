package ru.kolomiec.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.squareup.okhttp.Response;
import org.glassfish.grizzly.http.util.HttpStatus;
import ru.kolomiec.dto.TaskDTO;
import ru.kolomiec.requests.TaskApiRequest;
import ru.kolomiec.util.RequestUtil;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TaskService {

    private TaskApiRequest taskApiRequest = new TaskApiRequest();
    private AuthService authService = new AuthService();

    public void saveNewTaskToApi(Long chatId, TaskDTO taskDTO) {
        Response response = taskApiRequest.saveNewTaskToApi(chatId, taskDTO);
        if (response.code() == HttpStatus.FORBIDDEN_403.getStatusCode()) {
            authService.refreshToken(chatId);
            taskApiRequest.saveNewTaskToApi(chatId, taskDTO);
        }
    }

    public TaskDTO[] getAllTasksFromApi(Long chatId) {
        Response response = taskApiRequest.getAllTaskFromApi(chatId);
        RequestUtil requestUtil = new RequestUtil();
        String responseBodyJson = requestUtil.tryConvertResponseBodyToString(response.body());
        if (response.code() == HttpStatus.FORBIDDEN_403.getStatusCode()) {
            authService.refreshToken(chatId);
            getAllTasksFromApi(chatId);
        }
        return fromJsonToTaskArray(responseBodyJson);
    }
    private TaskDTO[] fromJsonToTaskArray(String json) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).findAndRegisterModules().readValue(json, TaskDTO[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String arrayDTOToString(TaskDTO[] tasks) {
        StringBuilder allTasks = new StringBuilder();
        for (TaskDTO t : tasks) {
            allTasks.append(t.getTaskName());
            if (t.getToDoTime() == null) {
                allTasks.append(" : ").append("no time").append("\n");
                continue;
            }
            allTasks.append(" : ").append(t.getToDoTime().getTaskDate().toString() + t.getToDoTime().getTaskTime().toString()).append("\n");
        }
        return allTasks.toString();
    }
}
