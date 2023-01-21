package ru.kolomiec.taskspring.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.services.PersonService;


@CrossOrigin
@RestController
@RequestMapping("/users")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public String sendUserTaskByUserId() {
        return null;
    }
}
