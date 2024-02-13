package avia.cloud.client.security;

import avia.cloud.client.dto.Authorization;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class AuthProvider {
    @Value("${application.jwt.key}")
    private String jwtKey;

    public Authorization createAuth(String username, String authorities) {
        SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
        String accessToken = Jwts.builder().setIssuer("Avionix").setSubject("ACCESS TOKEN")
                .claim("username", username)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 3600000))
                .signWith(key).compact();
        String refreshToken = Jwts.builder().setIssuer("Avionix").setSubject("REFRESH TOKEN")
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(key).compact();
        return new Authorization(
                "JWT",
                accessToken,
                3600000,
                refreshToken,
                604800000,
                authorities
        );
    }
}
