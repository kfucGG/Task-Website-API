package ru.kolomiec.taskspring.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.repository.PersonRepository;
import ru.kolomiec.taskspring.security.jwt.JwtRequest;
import ru.kolomiec.taskspring.services.interfaces.PersonService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Person findByUsername(String username) {
        //TODO custom exception
        return personRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("bad"));
    }

    @Override
    @Transactional
    public void savePerson(Person person) {
        person.setPassword(encodePassword(person.getPassword()));
        personRepository.save(person);
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
    public void isProcessAuthPersonPrincipalIsValid(JwtRequest jwtRequest) {
        if (!passwordEncoder.matches(jwtRequest.getPassword(), findByUsername(jwtRequest.getUsername()).getPassword())) {
            throw new BadCredentialsException("bad password");
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
