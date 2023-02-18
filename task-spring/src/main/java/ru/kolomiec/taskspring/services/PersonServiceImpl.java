package ru.kolomiec.taskspring.services;


import lombok.RequiredArgsConstructor;
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
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Person findByUsername(String username) {
        return personRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username does not exist"));
    }

    @Override
    @Transactional
    public Person savePerson(Person person) {
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
        personRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addTaskToPerson(Task task, Person person) {
        person.setUserTask(List.of(task));
        personRepository.save(person);
    }

    @Override
    public void isProcessAuthPersonCredentialsIsValid(JwtRequest jwtRequest) {
        if (!passwordEncoder.matches(jwtRequest.getPassword(), findByUsername(jwtRequest.getUsername()).getPassword())) {
            throw new BadCredentialsException("bad password");
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
