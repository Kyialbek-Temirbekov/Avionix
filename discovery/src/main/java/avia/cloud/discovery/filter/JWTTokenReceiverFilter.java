package avia.cloud.discovery.filter;

import avia.cloud.discovery.entity.enums.Role;
import avia.cloud.discovery.service.client.AuthorityFeignClient;
import avia.cloud.discovery.util.AuthorityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static avia.cloud.discovery.util.AuthorityUtils.getAuthorities;

@Component
@RequiredArgsConstructor
public class JWTTokenReceiverFilter extends OncePerRequestFilter {
    @Value("${application.jwt.key}")
    private String jwtKey;
    private final AuthorityFeignClient authorityFeignClient;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        Optional<String> auth = Optional.ofNullable(request.getHeader("Authorization"));
        return auth.map(s -> s.startsWith("Bearer")).orElse(!request.getServletPath().startsWith("/api"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        Authentication authentication;
        List<GrantedAuthority> grantedAuthorities;
        if(null != jwt) {
            if(jwt.startsWith("Basic")) {
                Authentication basicAuthentication = SecurityContextHolder.getContext().getAuthentication();
                List<Role> roles = basicAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).map(Role::valueOf).toList();
                grantedAuthorities = getAuthorities(authorityFeignClient.fetchAuthorities(roles.stream().map(Role::toString).collect(Collectors.joining(","))));
                roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(AuthorityUtils.addPrefix(role.toString()))));

                authentication = new UsernamePasswordAuthenticationToken(basicAuthentication.getName(), null, grantedAuthorities);
            }
            else {
                SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                String username = String.valueOf(claims.get("username"));
                String jwtAuthorities = (String) claims.get("authorities");

                List<Role> roles = Arrays.stream(jwtAuthorities.split(",")).map(role -> role.substring(5)).map(Role::valueOf).toList();
                grantedAuthorities = getAuthorities(authorityFeignClient.fetchAuthorities(roles.stream().map(Role::toString).collect(Collectors.joining(","))));
                roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(AuthorityUtils.addPrefix(role.toString()))));

                authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
            }
        }
        else {
            grantedAuthorities = getAuthorities(authorityFeignClient.fetchAuthorities("GUEST"));
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
            authentication = new UsernamePasswordAuthenticationToken("guest", null, grantedAuthorities);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
