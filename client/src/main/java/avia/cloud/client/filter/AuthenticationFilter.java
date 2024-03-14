package avia.cloud.client.filter;

import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.service.IAuthorityService;
import avia.cloud.client.util.AuthorityUtils;
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
import java.util.*;

import static avia.cloud.client.util.AuthorityUtils.extractClaim;
import static avia.cloud.client.util.AuthorityUtils.getAuthorities;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    @Value("${application.jwt.key}")
    private String jwtKey;
    private final IAuthorityService iAuthorityService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().startsWith("/api");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        Authentication authentication;

        if(null != token) {
            if(token.startsWith("Basic")) {
                Authentication basicAuth = SecurityContextHolder.getContext().getAuthentication();
                List<Role> roles = basicAuth.getAuthorities().stream().map(GrantedAuthority::getAuthority).map(Role::valueOf).toList();
                List<GrantedAuthority> grantedAuthorities = getAuthorities(iAuthorityService.fetchAuthorities(roles));
                roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(AuthorityUtils.addPrefix(role.toString()))));

                authentication = new UsernamePasswordAuthenticationToken(basicAuth.getName(), null, grantedAuthorities);
            }
            else if(token.startsWith("Bearer")) {
                String username = extractClaim(token,"email");
                List<GrantedAuthority> grantedAuthorities = getAuthorities(iAuthorityService.fetchAuthorities(Collections.singletonList(Role.CLIENT)));
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
                authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
            }
            else  {
                SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                String username = String.valueOf(claims.get("username"));
                String jwtAuthorities = (String) claims.get("authorities");

                List<Role> roles = Arrays.stream(jwtAuthorities.split(",")).map(role -> role.substring(5)).map(Role::valueOf).toList();
                List<GrantedAuthority> grantedAuthorities = getAuthorities(iAuthorityService.fetchAuthorities(roles));
                roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(AuthorityUtils.addPrefix(role.toString()))));

                authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
            }

        }
        else {
            List<GrantedAuthority> grantedAuthorities = getAuthorities(iAuthorityService.fetchAuthorities(Collections.singletonList(Role.GUEST)));
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
            authentication = new UsernamePasswordAuthenticationToken("guest", null, grantedAuthorities);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
