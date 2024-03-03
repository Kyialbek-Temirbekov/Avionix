package avia.cloud.client.filter;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JWTTokenSupplierFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private ObjectMapper objectMapper;

    @Override
    protected void initFilterBean() throws ServletException {
        objectMapper = new ObjectMapper();
        super.initFilterBean();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/api/account/signIn");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null != authentication) {
            Authorization auth = jwtService.createToken(authentication.getName(),populateAuthorities(authentication.getAuthorities()));
            String json = objectMapper.writeValueAsString(auth);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        }
        filterChain.doFilter(request, response);
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for(GrantedAuthority authority : authorities)
            if (authority.getAuthority().startsWith("ROLE_")) {
                authoritiesSet.add(authority.getAuthority());
            }
        return String.join(",", authoritiesSet);
    }
}
