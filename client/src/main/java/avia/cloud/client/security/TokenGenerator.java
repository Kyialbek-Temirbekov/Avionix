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
public class TokenGenerator {
    @Value("${application.jwt.key}")
    private String jwtKey;

    public Authorization generate(String username, String authorities) {
        return new Authorization(
                "JWT",
                getToken(username,authorities,"ACCESS_TOKEN",3600000),
                3600000,
                getToken(username,authorities,"REFRESH_TOKEN",604800000),
                604800000,
                authorities
        );
    }
    public String getToken(String username, String authorities, String subject, int expiryTime) {
        SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().setIssuer("Avionix").setSubject(subject)
                .claim("username", username)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiryTime))
                .signWith(key).compact();
    }
}
