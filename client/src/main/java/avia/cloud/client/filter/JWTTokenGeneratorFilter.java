package avia.cloud.client.filter;

import avia.cloud.client.dto.Auth;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
    @Value("${application.jwt.key}")
    private String jwtKey;
    private ObjectMapper objectMapper;

    @Override
    protected void initFilterBean() throws ServletException {
        objectMapper = new ObjectMapper();
        super.initFilterBean();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Stream.of("/api/airline/confirmEmail","api/customer/confirmEmail","/api/customer/signIn")
                .noneMatch(path -> path.equals(request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null != authentication) {
            Auth auth = getAuth(authentication);
            String json = objectMapper.writeValueAsString(auth);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        }
        filterChain.doFilter(request, response);
    }

    private Auth getAuth(Authentication authentication) {
        SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
        String accessToken = Jwts.builder().setIssuer("Avionix").setSubject("ACCESS TOKEN")
                .claim("username", authentication.getName())
                .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 3600000))
                .signWith(key).compact();
        String refreshToken = Jwts.builder().setIssuer("Avionix").setSubject("REFRESH TOKEN")
                .claim("username", authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(key).compact();
        return new Auth(
                "JWT",
                accessToken,
                3600000,
                refreshToken,
                604800000,
                populateAuthorities(authentication.getAuthorities())
        );
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for(GrantedAuthority authority : authorities)
            authoritiesSet.add(authority.getAuthority());
        return String.join(",", authoritiesSet);
    }
}
