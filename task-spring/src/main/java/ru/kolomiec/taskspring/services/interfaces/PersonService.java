package ru.kolomiec.taskspring.services.interfaces;


import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.security.jwt.JwtRequest;

public interface PersonService {

    Person findByUsername(String username);

    Person savePerson(Person person);
    Person savePerson(PersonRegistrationDTO personRegistrationDTO);

    void deletePerson(Long id);

    void addTaskToPerson(Task task, Person person);

    void isProcessAuthPersonCredentialsIsValid(JwtRequest processAuthPerson);


}
