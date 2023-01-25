package ru.kolomiec.taskspring.controllers;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.facade.PersonFacade;
import ru.kolomiec.taskspring.security.jwt.JwtRequest;
import ru.kolomiec.taskspring.security.jwt.JwtResponse;
import ru.kolomiec.taskspring.security.jwt.JwtUtil;
import ru.kolomiec.taskspring.services.interfaces.PersonService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication controller", description = "authentication controller with jwt token")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PersonService personService;

    @PostMapping("/login")
    @Operation(summary = "login with username and password and returnes jwt token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
        personService.isProcessAuthPersonPrincipalIsValid(jwtRequest);
        return ResponseEntity.ok(jwtUtil.generateToken(jwtRequest.getUsername()));
    }

    @PostMapping("/registration")
    @Operation(summary = "registration new person and returns jwt token of this person")
    public ResponseEntity<JwtResponse> registration(@RequestBody PersonRegistrationDTO person) {
        personService.savePerson(person);
        return ResponseEntity.ok(jwtUtil.generateToken(person.getUsername()));
    }

}
