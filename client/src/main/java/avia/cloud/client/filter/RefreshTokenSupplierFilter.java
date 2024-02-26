package avia.cloud.client.filter;

import avia.cloud.client.entity.Account;
import avia.cloud.client.security.TokenGenerator;
import avia.cloud.client.service.IAccountService;
import avia.cloud.client.util.RoleConverter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RefreshTokenSupplierFilter extends OncePerRequestFilter {
    @Value("${application.jwt.key}")
    private String jwtKey;
    private final TokenGenerator tokenGenerator;
    private final IAccountService iUserService;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/api/account/refresh");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Refresh");
        if(null == jwt) {
            throw new BadCredentialsException("Required refresh token");
        }
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            String username = String.valueOf(claims.get("username"));
            if(!claims.getSubject().equals("REFRESH_TOKEN")) {
                throw new BadCredentialsException("Required refresh token");
            }
            Account user = iUserService.fetchUser(username);
            String roles = user.getRoles().stream().map(Enum::toString).map(RoleConverter::convert).collect(Collectors.joining(","));
            String refreshToken = tokenGenerator.getToken(user.getEmail(),roles,"ACCESS_TOKEN",3600000);
            response.setHeader("Authorization", refreshToken);
        }
        catch (Exception e) {
            throw new BadCredentialsException("Invalid token received");
        }
        filterChain.doFilter(request, response);

    }
}
