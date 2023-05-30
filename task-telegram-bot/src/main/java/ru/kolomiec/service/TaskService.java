package ru.kolomiec.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.util.HttpStatus;
import ru.kolomiec.dto.TaskDTO;
import ru.kolomiec.dto.ToDoTime;
import ru.kolomiec.requests.TaskApiRequest;
import ru.kolomiec.util.RequestUtil;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
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
        log.info(String.format(
                "User with chatId: %s try to getting tasks from api", chatId
        ));
        Response response = taskApiRequest.getAllTaskFromApi(chatId);
        RequestUtil requestUtil = new RequestUtil();
        String responseBodyJson = requestUtil.tryConvertResponseBodyToString(response.body());
        if (response.code() == HttpStatus.FORBIDDEN_403.getStatusCode()) {
            log.info("Token of user with chatid: " + chatId + " is expired, refreshing");
            authService.refreshToken(chatId);
            getAllTasksFromApi(chatId);
        }
        if (response.code() == HttpStatus.BAD_REQUEST_400.getStatusCode())  {
            log.info("user with chat id: " + chatId + " dont have tasks on api, creating and return error msg");
            return new TaskDTO[] {
                    TaskDTO.builder()
                            .taskName("У вас нету задач ")
                            .toDoTime(
                                    ToDoTime.builder()
                                            .taskDate(LocalDate.now())
                                            .taskTime(LocalTime.now())
                                            .build()
                    ).build()
            };
        }

        return fromJsonToTaskArray(responseBodyJson);
    }

    public void deleteTask(Long chatId, Long taskId) {
        Response response = taskApiRequest.deleteUserTask(chatId, taskId);
        if (response.code() != HttpStatus.NO_CONTENT_204.getStatusCode()) {
            throw new RuntimeException("exception on api, can not delete task");
        }
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
