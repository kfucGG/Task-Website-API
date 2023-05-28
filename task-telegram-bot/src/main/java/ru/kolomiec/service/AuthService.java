package ru.kolomiec.service;


import lombok.extern.slf4j.Slf4j;
import ru.kolomiec.database.dao.PersonDAO;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.database.entity.Person;
import ru.kolomiec.dto.PersonDTO;
import ru.kolomiec.requests.AuthApiRequest;

@Slf4j
public class AuthService {

    private final AuthApiRequest authRequest = new AuthApiRequest();
    private final PersonDAO personDAO = new PersonDAO();

    public void registration(Person person) {
        log.info(String.format("Person: %s try to registration", person.getUsername()));
        AuthToken jwtToken = authRequest.registrationNewPersonOnApi(new PersonDTO(person.getUsername(), person.getPassword()));
        personDAO.savePerson(person, jwtToken);
    }

    public void refreshToken(Long chatId) {
        Person person = personDAO.findPersonByChatId(chatId);
        AuthToken newToken = authRequest.loginPersonOnApi(new PersonDTO(person.getUsername(), person.getPassword()));
        personDAO.updatePerson(person, newToken);
    }

    public void login(Person person) {
        log.info(String.format("Person: %s try to login", person.getUsername()));
        AuthToken accessToken = authRequest.loginPersonOnApi(new PersonDTO(person.getUsername(), person.getPassword()));
        personDAO.savePerson(person, accessToken);
    }
}
