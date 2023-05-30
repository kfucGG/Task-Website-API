package ru.kolomiec.requests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;
import ru.kolomiec.database.dao.PersonDAO;
import ru.kolomiec.database.entity.Person;
import ru.kolomiec.dto.TaskDTO;


@Slf4j
public class TaskApiRequest extends AbstractApiRequest {

    private PersonDAO personDAO = new PersonDAO();

    public Response saveNewTaskToApi(Long chatId, TaskDTO task) {
        log.info(String.format(
                "User with chatId: %s try to adding task:%s", chatId, task.getTaskName()
        ));
        Person person = personDAO.findPersonByChatId(chatId);
        Request request = new Request.Builder()
                .url(getBaseApiUrl() + "/task/add-task")
                .header("Authorization", person.getAuthToken().getToken())
                .post(RequestBody.create(applicationJsonMediaType, entityToJson(task)))
                .build();
        return requestUtil.tryRequest(request);
    }

    public Response deleteUserTask(Long chatId, Long taskId) {
        log.info(
                String.format("sending request on api for deleting task with id: %s", taskId)
        );
        Person person = personDAO.findPersonByChatId(chatId);
        Request taskDeleteRequest = new Request.Builder()
                .url(getBaseApiUrl() + "/task/" + taskId)
                .header("Authorization", person.getAuthToken().getToken())
                .delete()
                .build();
        return requestUtil.tryRequest(taskDeleteRequest);
    }
    public Response getAllTaskFromApi(Long chatId) {
        Person person = personDAO.findPersonByChatId(chatId);
        Request request = new Request.Builder()
                .url(getBaseApiUrl() + "/task/all-tasks")
                .header("Authorization", person.getAuthToken().getToken())
                .build();
        return requestUtil.tryRequest(request);
    }
}
