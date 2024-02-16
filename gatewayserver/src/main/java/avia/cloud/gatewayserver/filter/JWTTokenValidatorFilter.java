package avia.cloud.gatewayserver.filter;

import avia.cloud.gatewayserver.dto.RequestDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
@Order(1)
public class JWTTokenValidatorFilter implements GlobalFilter {
    @Value("${application.jwt.key}")
    private String jwtKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (isApplicable(exchange)) {
            String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
            try {
                SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                if(!claims.getSubject().equals("ACCESS_TOKEN")) {
                    throw new RuntimeException();
                }
                setAuthentication(claims);
            }
            catch (Exception e) {
                throw new BadCredentialsException("Invalid token received");
            }
        }

        return chain.filter(exchange);
    }

    private boolean isApplicable(ServerWebExchange exchange) {
        Optional<String> auth = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("Authorization"));
        boolean isJwt = false;
        if(auth.isPresent()) {
            isJwt = !auth.get().startsWith("Bearer");
        }
        List<RequestDTO> requests = RequestDTO.builder()
                .pathMatchers(HttpMethod.POST, "/api/customer","/api/customer/signIn","/api/airline")
                .pathMatchers(HttpMethod.PATCH,"/api/customer/confirmEmail","/api/airline","/api/airline/confirmEmail")
                .pathMatchers(HttpMethod.DELETE,"/api/customer/removeAll","/api/airline/removeAll")
                .build();
        return requests.stream().noneMatch(r -> exchange.getRequest().getMethod().equals(r.getHttpMethod()) &&
                exchange.getRequest().getPath().toString().startsWith(r.getPath())) && isJwt;
    }

    private void setAuthentication(Claims claims) {
        String username = String.valueOf(claims.get("username"));
        String authorities = (String) claims.get("authorities");
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
