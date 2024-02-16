package avia.cloud.client.filter;

import avia.cloud.client.entity.AccountBase;
import avia.cloud.client.security.AuthProvider;
import avia.cloud.client.service.ICustomerService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RefreshTokenGeneratorFilter extends OncePerRequestFilter {
    @Value("${application.jwt.key}")
    private String jwtKey;
    private final AuthProvider authProvider;
    private final ICustomerService iCustomerService;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/api/customer/refresh");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("RefreshToken");
        if(null != jwt) {
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
                AccountBase account = iCustomerService.fetchAccount(username);
                String roles = account.getRoles().stream().map(Enum::toString).map(RoleConverter::convert).collect(Collectors.joining(","));
                String refreshToken = authProvider.getToken(account.getEmail(),roles,"ACCESS_TOKEN",3600000);
                response.setHeader("AccessToken", refreshToken);
            }
            catch (Exception e) {
                throw new BadCredentialsException("Invalid token received");
            }
        }
        filterChain.doFilter(request, response);

    }
}
