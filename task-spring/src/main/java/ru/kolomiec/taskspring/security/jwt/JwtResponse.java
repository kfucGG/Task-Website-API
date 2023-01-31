package ru.kolomiec.taskspring.security.jwt;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class JwtResponse {

    private String token;
}
