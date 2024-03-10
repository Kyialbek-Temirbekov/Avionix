package avia.cloud.client.filter;

import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.service.IAuthorityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static avia.cloud.client.util.AuthorityUtils.extractClaim;
import static avia.cloud.client.util.AuthorityUtils.getAuthorities;

@Component
@RequiredArgsConstructor
public class IdTokenReceiverFilter extends OncePerRequestFilter {
    private final JwtDecoder jwtDecoder;
    private final IAuthorityService iAuthorityService;

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
        String username = extractClaim(jwtToken,"email");
        List<GrantedAuthority> grantedAuthorities = getAuthorities(iAuthorityService.fetchAuthorities(Collections.singletonList(Role.CLIENT)));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }

}
