package ru.kolomiec.taskspring.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private final String SECRET = "SECRET";

    public JwtResponse generateToken(String username){
        log.info("generating token to person with username: " + username);
        return new JwtResponse(JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withIssuer("task-api")
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256("SECRET")));
    }

    public JWTVerifier validateToken() {
        return JWT.require(Algorithm.HMAC256("SECRET"))
                .withSubject("User details")
                .withIssuer("task-api")
                .build();
    }

    public String getUsernameClaimFromToken(String token) {
        return validateToken().verify(token).getClaim("username").asString();
    }
}
