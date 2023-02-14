package ru.kolomiec.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.squareup.okhttp.Response;
import org.glassfish.grizzly.http.util.HttpStatus;
import ru.kolomiec.dto.TaskDTO;
import ru.kolomiec.requests.TaskApiRequest;
import ru.kolomiec.util.RequestUtil;


import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class TaskService {

    private TaskApiRequest taskApiRequest = new TaskApiRequest();
    private AuthService authService = new AuthService();
    private final String timeRegexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T(2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]";
    public void saveNewTaskToApi(Long chatId, String[] userInput) {
        Response response = taskApiRequest.saveNewTaskToApi(chatId, buildTaskDTOFromArrayOfString(userInput));
        if (response.code() == HttpStatus.FORBIDDEN_403.getStatusCode()) {
            authService.refreshToken(chatId);
            taskApiRequest.saveNewTaskToApi(chatId, buildTaskDTOFromArrayOfString(userInput));
        }
    }

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
    private TaskDTO buildTaskDTOFromArrayOfString(String[] strings) {
        StringBuilder taskName = new StringBuilder();
        LocalDateTime taskToDoTime = null;
        for (String s : strings) {
            if (Pattern.compile(timeRegexp).matcher(s).matches()) {
                taskToDoTime = LocalDateTime.parse(s);
                continue;
            }
            taskName.append(s);
        }
        return new TaskDTO(taskName.toString(), taskToDoTime);
    }

}
