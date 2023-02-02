package ru.kolomiec.service;

import com.google.gson.Gson;
import com.squareup.okhttp.Response;
import org.glassfish.grizzly.http.util.HttpStatus;
import ru.kolomiec.dto.TaskDTO;
import ru.kolomiec.requests.TaskApiRequest;
import ru.kolomiec.util.RequestUtil;

import java.util.Arrays;

public class TaskService {

    private TaskApiRequest taskApiRequest = new TaskApiRequest();
    private AuthService authService = new AuthService();

    public void saveNewTaskToApi(Long chatId, String[] userInput) {
        Response response = taskApiRequest.saveNewTaskToApi(chatId, buildTaskDTOFromArrayOfString(userInput));
        if (response.code() == HttpStatus.FORBIDDEN_403.getStatusCode()) {
            authService.refreshToken(chatId);
            taskApiRequest.saveNewTaskToApi(chatId, buildTaskDTOFromArrayOfString(userInput));
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
        return new Gson().fromJson(json, TaskDTO[].class);
    }
    private TaskDTO buildTaskDTOFromArrayOfString(String[] strings) {
        StringBuilder taskName = new StringBuilder();
        Arrays.stream(strings).forEach(a -> taskName.append(a).append(" "));
        return new TaskDTO(taskName.toString());
    }

}
