package ru.kolomiec.taskspring.initialize;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.repository.PersonRepository;


import java.util.List;

@Component
public class DatabaseInit {

    private final PersonRepository personRepository;

    public DatabaseInit(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostConstruct
    public void initTestEntity() {
        Person person = new Person();
        Task task = new Task();
        task.setTaskName("do homework");
        task.setOwner(person);

        Task task2 = new Task();
        task2.setTaskName("do something");
        task2.setOwner(person);

        person.setUsername("test");
        person.setPassword("test");
        person.setUserTask(List.of(task, task2));
        personRepository.save(person);
    }
}
