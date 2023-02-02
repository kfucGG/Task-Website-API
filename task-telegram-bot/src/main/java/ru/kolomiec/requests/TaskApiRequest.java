package ru.kolomiec.requests;

import com.squareup.okhttp.*;
import ru.kolomiec.database.dao.AuthTokenDAO;
import ru.kolomiec.database.dao.PersonDAO;
import ru.kolomiec.database.entity.Person;
import ru.kolomiec.dto.TaskDTO;


public class TaskApiRequest extends AbstractApiRequest {

    private PersonDAO personDAO = new PersonDAO();
    private AuthTokenDAO authTokenDAO = new AuthTokenDAO();

    public void saveNewTaskToApi(Long chatId) {
        Person person = personDAO.findPersonByChatId(chatId);
        Request request = new Request.Builder()
                .url(BASE_API_URL + "/task/add-task")
                .header("Authorization", person.getAuthToken().getToken())
                .post(RequestBody.create(applicationJsonMediaType, entityToJson(new TaskDTO("homework"))))
                .build();
        Response response = tryRequest(request);
        System.out.println(response.code());
    }

    public void getAllTaskFromApi(Long chatId) {
        Person person = personDAO.findPersonByChatId(chatId);
        Request request = new Request.Builder()
                .url(BASE_API_URL + "/task/all-tasks")
                .header("Authorization", person.getAuthToken().getToken())
                .build();
        Response response = tryRequest(request);
        System.out.println(response.code());
    }
}
