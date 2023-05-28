package ru.kolomiec.taskspring.controllers;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.exceptions.customexceptions.PersonWithSuchUsernameAlreadyExists;
import ru.kolomiec.taskspring.security.jwt.JwtRequest;
import ru.kolomiec.taskspring.security.jwt.JwtResponse;
import ru.kolomiec.taskspring.security.jwt.JwtUtil;
import ru.kolomiec.taskspring.services.interfaces.PersonService;
import ru.kolomiec.taskspring.util.validators.UniqueUsernameValidator;

import java.util.Arrays;
import java.util.function.Consumer;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication controller", description = "authentication controller with jwt token")
@Slf4j
public class AuthRestController {

    private final JwtUtil jwtUtil;
    private final PersonService personService;
    private final UniqueUsernameValidator uniqueUsernameValidator;


    @PostMapping("/login")
    @Operation(summary = "login with username and password and return jwt token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person is auth, return jwt token"),
            @ApiResponse(responseCode = "400", description = "Wrong password or defunct username")
    })
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid JwtRequest jwtRequest) {
        log.info("Person with username: " + jwtRequest.getUsername() + " try to login");
        personService.isProcessAuthPersonCredentialsIsValid(jwtRequest);
        return ResponseEntity.ok(jwtUtil.generateToken(jwtRequest.getUsername()));
    }

    @PostMapping("/registration")
    @Operation(summary = "registration new person and returns jwt token of this person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New person is reg and token is returned"),
            @ApiResponse(responseCode = "400", description = "Not valid person fields")
    })
    public ResponseEntity<JwtResponse> registration(@RequestBody @Valid PersonRegistrationDTO person,
                                                    BindingResult bindingResult) {
        log.info("person with username " + person.getUsername() + " try to registration");
        uniqueUsernameValidator.validate(person, bindingResult);
        bindingResult.getAllErrors().stream()
                .filter(a -> {
                    return Arrays.stream(a.getCodes()).anyMatch(
                            codes -> codes.equals(String.valueOf(HttpStatus.CONFLICT.value()))
                    );
                }).findAny().ifPresent((error) -> {
                    log.info("person with " + person.getUsername() + " already exists");
                    throw new PersonWithSuchUsernameAlreadyExists();
                });
        personService.savePerson(person);
        return ResponseEntity.ok(jwtUtil.generateToken(person.getUsername()));
    }

}
