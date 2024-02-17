package avia.cloud.gatewayserver.filter;

import avia.cloud.gatewayserver.dto.ErrorResponseDTO;
import avia.cloud.gatewayserver.dto.RequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Order(1)
@RequiredArgsConstructor
public class JWTTokenValidatorFilter implements GlobalFilter {
    @Value("${application.jwt.key}")
    private String jwtKey;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
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
        } catch (BadCredentialsException e) {
            return handleError(e,exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> handleError(Exception exception, ServerWebExchange exchange) throws JsonProcessingException {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                exchange.getRequest().getPath().toString(),
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                LocalDateTime.now().toString()
        );
        String jsonResponse = objectMapper.writeValueAsString(errorResponseDTO);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().setContentLength(jsonResponse.length());
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private boolean isApplicable(ServerWebExchange exchange) {
        Optional<String> auth = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("Authorization"));
        List<RequestDTO> requests = RequestDTO.builder()
                .pathMatchers(HttpMethod.POST, "/api/customer","/api/customer/signIn","/api/airline")
                .pathMatchers(HttpMethod.PATCH,"/api/customer/confirmEmail","/api/airline","/api/airline/confirmEmail")
                .pathMatchers(HttpMethod.DELETE,"/api/customer/removeAll","/api/airline/removeAll")
                .pathMatchers(HttpMethod.GET,"/api/customer/refresh")
                .build();
        boolean isSecured = requests.stream().noneMatch(r -> exchange.getRequest().getMethod().equals(r.getHttpMethod()) &&
                exchange.getRequest().getPath().toString().startsWith(r.getPath()));
        if(isSecured && auth.isEmpty()) {
            throw new BadCredentialsException("Access token required");
        }
        boolean isJwt = false;
        if (auth.isPresent()) {
            isJwt = !auth.get().startsWith("Bearer");
        }
        return isSecured && isJwt;
    }

    private void setAuthentication(Claims claims) {
        String username = String.valueOf(claims.get("username"));
        String authorities = (String) claims.get("authorities");
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
