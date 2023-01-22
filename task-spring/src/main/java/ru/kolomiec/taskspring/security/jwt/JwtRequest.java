package ru.kolomiec.taskspring.security.jwt;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class JwtRequest {

    private final String username;
    private final String password;
}
