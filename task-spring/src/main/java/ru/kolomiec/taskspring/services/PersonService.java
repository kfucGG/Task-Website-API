package ru.kolomiec.taskspring.services;


import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.entity.Task;

public interface PersonService {

    Person findByUsername(String username);

    void savePerson(Person person);

    void deletePerson(Long id);

    void addTaskToPerson(Task task, Person person);
}
