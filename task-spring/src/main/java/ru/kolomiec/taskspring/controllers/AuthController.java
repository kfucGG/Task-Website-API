package ru.kolomiec.taskspring.controllers;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.security.jwt.JwtRequest;
import ru.kolomiec.taskspring.security.jwt.JwtResponse;
import ru.kolomiec.taskspring.security.jwt.JwtUtil;
import ru.kolomiec.taskspring.services.interfaces.PersonService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication controller", description = "authentication controller with jwt token")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PersonService personService;

    //todo add exception handler for badCredentials
    @PostMapping("/login")
    @Operation(summary = "login with username and password and returnes jwt token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
        personService.isProcessAuthPersonPrincipalIsValid(jwtRequest);
        return ResponseEntity.ok(jwtUtil.generateToken(jwtRequest.getUsername()));
    }

    @PostMapping("/registration")//todo maybe should create entity class for registration(DTO maybe)
    @Operation(summary = "registration new person and returns jwt token of this person")
    public ResponseEntity<JwtResponse> registration(@RequestBody Person person) {
        personService.savePerson(person);//todo add validation and exception handlers (ControllerAdvice.class)
        return ResponseEntity.ok(jwtUtil.generateToken(person.getUsername()));
    }

}
