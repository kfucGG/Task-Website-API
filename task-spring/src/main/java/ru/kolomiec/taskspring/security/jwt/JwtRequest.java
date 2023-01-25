package ru.kolomiec.taskspring.security.jwt;


import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class JwtRequest {

    private final String username;
    private final String password;
}
