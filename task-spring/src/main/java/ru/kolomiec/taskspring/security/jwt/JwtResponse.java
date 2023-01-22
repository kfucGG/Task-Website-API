package ru.kolomiec.taskspring.security.jwt;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class JwtResponse {

    private final String token;
}
