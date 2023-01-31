package ru.kolomiec.taskspring.initialize;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.repository.PersonRepository;


import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInit {

    private final PersonRepository personRepository;


    @PostConstruct
    public void initTestEntity() {
        //todo make normal init method for database
    }
}
