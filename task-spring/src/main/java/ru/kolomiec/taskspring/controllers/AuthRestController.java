package ru.kolomiec.taskspring.controllers;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kolomiec.taskspring.dto.PersonRegistrationDTO;
import ru.kolomiec.taskspring.security.jwt.JwtRequest;
import ru.kolomiec.taskspring.security.jwt.JwtResponse;
import ru.kolomiec.taskspring.security.jwt.JwtUtil;
import ru.kolomiec.taskspring.services.interfaces.PersonService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication controller", description = "authentication controller with jwt token")
public class AuthRestController {

    private final JwtUtil jwtUtil;
    private final PersonService personService;

    @PostMapping("/login")
    @Operation(summary = "login with username and password and return jwt token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person is auth, return jwt token"),
            @ApiResponse(responseCode = "400", description = "Wrong password or defunct username")
    })
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
        personService.isProcessAuthPersonPrincipalIsValid(jwtRequest);
        return ResponseEntity.ok(jwtUtil.generateToken(jwtRequest.getUsername()));
    }

    @PostMapping("/registration")
    @Operation(summary = "registration new person and returns jwt token of this person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New person is reg and token is returned"),
            @ApiResponse(responseCode = "400", description = "Not valid person fields")
    })
    public ResponseEntity<JwtResponse> registration(@RequestBody PersonRegistrationDTO person) {
        personService.savePerson(person);
        return ResponseEntity.ok(jwtUtil.generateToken(person.getUsername()));
    }

}
