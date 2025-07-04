package br.com.hyper.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtUtil {

    private final Algorithm algorithm;

    public JwtUtil(@Value("${api.security.token.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateAccessToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    public String generateRefreshToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .sign(algorithm);
    }

    public String validateToken(String token) {
        return JWT.require(algorithm).build().verify(token).getSubject();
    }
}

