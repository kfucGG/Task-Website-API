package ru.kolomiec.taskspring.security.jwt;


import lombok.*;

import java.io.Serializable;



@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest implements Serializable {

    private String username;
    private String password;
}
