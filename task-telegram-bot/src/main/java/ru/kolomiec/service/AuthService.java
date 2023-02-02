package ru.kolomiec.service;

import ru.kolomiec.database.dao.PersonDAO;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.database.entity.Person;
import ru.kolomiec.dto.PersonDTO;
import ru.kolomiec.requests.AuthApiRequest;

public class AuthService {

    private final AuthApiRequest authRequest = new AuthApiRequest();
    private final PersonDAO personDAO = new PersonDAO();

    public void registration(Person person) {
        AuthToken jwtToken = authRequest.registrationNewPersonOnApi(new PersonDTO(person.getUsername(), person.getPassword()));
        personDAO.savePerson(person, jwtToken);
    }

    public void refreshToken(Long chatId) {
        Person person = personDAO.findPersonByChatId(chatId);
        AuthToken newToken = authRequest.loginPersonOnApi(new PersonDTO(person.getUsername(), person.getPassword()));
        personDAO.updatePerson(person, newToken);
    }

    public void login(Person person) {
        AuthToken accessToken = authRequest.loginPersonOnApi(new PersonDTO(person.getUsername(), person.getPassword()));
        personDAO.savePerson(person, accessToken);
    }
}
