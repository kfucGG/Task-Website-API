package ru.kolomiec.requests;


import com.squareup.okhttp.*;
import ru.kolomiec.database.dao.PersonDAO;
import ru.kolomiec.database.entity.Person;
import ru.kolomiec.dto.TaskDTO;



public class TaskApiRequest extends AbstractApiRequest {

    private PersonDAO personDAO = new PersonDAO();

    public Response saveNewTaskToApi(Long chatId, TaskDTO task) {
        Person person = personDAO.findPersonByChatId(chatId);
        Request request = new Request.Builder()
                .url(BASE_API_URL + "/task/add-task")
                .header("Authorization", person.getAuthToken().getToken())
                .post(RequestBody.create(applicationJsonMediaType, entityToJson(task)))
                .build();
        return requestUtil.tryRequest(request);
    }

    public Response getAllTaskFromApi(Long chatId) {
        Person person = personDAO.findPersonByChatId(chatId);
        Request request = new Request.Builder()
                .url(BASE_API_URL + "/task/all-tasks")
                .header("Authorization", person.getAuthToken().getToken())
                .build();
        return requestUtil.tryRequest(request);
    }
}
