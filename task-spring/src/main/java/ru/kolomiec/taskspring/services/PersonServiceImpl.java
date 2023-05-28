package ru.kolomiec.taskspring.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolomiec.taskspring.aspects.ServiceLog;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.repository.PersonRepository;
import ru.kolomiec.taskspring.security.jwt.JwtRequest;
import ru.kolomiec.taskspring.services.interfaces.PersonService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@ServiceLog
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Person findByUsername(String username) {
        return personRepository.findByUsername(username).orElseThrow(
                () -> {
                    log.info(String.format(
                            "Person with username %s already exists", username
                            )
                    );
                    return new UsernameNotFoundException("Username does not exist");
                });
    }

    @Override
    @Transactional
    public Person savePerson(Person person) {
        log.info(String.format(
                "saving person with next attributes with username %s", person.getUsername()
        ));
        person.setPassword(encodePassword(person.getPassword()));
        return personRepository.save(person);
    }

    @Override
    @Transactional
    public Person savePerson(PersonRegistrationDTO personRegistration) {
        return savePerson(personRegistration.toPerson());
    }

    @Override
    @Transactional
    public void deletePerson(Long id) {
        log.info(String.format(
                "Deleting person with id: %s", id
        ));
        personRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addTaskToPerson(Task task, Person person) {
        log.info(String.format(
                "adding task to person with username: %s, task name %s, to do time is: %s",
                person.getUsername(), task.getTaskName(), task.getToDoTime().toString()
        ));
        person.setUserTask(List.of(task));
        personRepository.save(person);
    }

    @Override
    public void isProcessAuthPersonCredentialsIsValid(JwtRequest jwtRequest) {
        log.info(String.format(
                "Check that password which was get from person with username: %s is valid", jwtRequest.getUsername()
        ));
        if (!passwordEncoder.matches(jwtRequest.getPassword(), findByUsername(jwtRequest.getUsername()).getPassword())) {
            log.info(String.format("Person: %s provided invalid password", jwtRequest.getUsername()));
            throw new BadCredentialsException("bad password");
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
