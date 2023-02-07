package ru.kolomiec.taskspring.initialize;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.repository.PersonRepository;


import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInit {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initTestEntity() {
        Person person = new Person();
        person.setUsername("username");
        person.setPassword(passwordEncoder.encode("password"));
        personRepository.save(person);
    }
}
