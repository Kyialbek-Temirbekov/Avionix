package avia.cloud.client.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class IdTokenReceiverFilter extends OncePerRequestFilter {
    private final JwtDecoder jwtDecoder;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String auth = request.getHeader("Authorization");
        if(auth != null) {
            return !auth.startsWith("Bearer");
        }
        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");
        Map<String,Object> claims = null;
        try {
            claims = jwtDecoder.decode(jwtToken.substring(7)).getClaims();
        } catch (JwtException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        String username = (String) claims.get("username");
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
