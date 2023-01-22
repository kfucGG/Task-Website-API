package ru.kolomiec.taskspring.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.repository.PersonRepository;
import ru.kolomiec.taskspring.security.jwt.JwtRequest;
import ru.kolomiec.taskspring.security.jwt.JwtResponse;
import ru.kolomiec.taskspring.security.jwt.JwtUtil;
import ru.kolomiec.taskspring.services.PersonServiceImpl;
import ru.kolomiec.taskspring.services.interfaces.PersonService;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PersonService personService;

    //todo add exception handler for badCredentials
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
        personService.isProcessAuthPersonPrincipalIsValid(jwtRequest);
        return ResponseEntity.ok(jwtUtil.generateToken(jwtRequest.getUsername()));
    }

    @PostMapping("/registration")//todo maybe should create entity class for registration(DTO maybe)
    public ResponseEntity<JwtResponse> registration(@RequestBody Person person) {
        personService.savePerson(person);//todo add validation and exception handlers (ControllerAdvice.class)
        return ResponseEntity.ok(jwtUtil.generateToken(person.getUsername()));
    }

}
