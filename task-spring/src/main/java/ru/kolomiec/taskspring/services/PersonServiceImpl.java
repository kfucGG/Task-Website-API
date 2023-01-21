package ru.kolomiec.taskspring.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.repository.PersonRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService{

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person findByUsername(String username) {
        return personRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("bad"));
        //TODO custom exception
    }

    @Override
    @Transactional
    public void savePerson(Person person) {
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
}
