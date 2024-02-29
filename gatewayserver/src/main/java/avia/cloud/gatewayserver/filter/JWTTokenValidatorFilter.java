package avia.cloud.gatewayserver.filter;

import avia.cloud.gatewayserver.dto.ErrorResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@Order(1)
@RequiredArgsConstructor
public class JWTTokenValidatorFilter implements WebFilter {
    @Value("${application.jwt.key}")
    private String jwtKey;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,WebFilterChain  chain) {
        Object filterAttribute = exchange.getAttribute(".FILTERED");
        boolean filtered = filterAttribute != null && (boolean)filterAttribute;
        exchange.getAttributes().put(".FILTERED", true);

        if (filtered) {
            return doFilterInternal(exchange, chain);
        }
        else {
            return chain.filter(exchange);
        }
    }

    @SneakyThrows
    private Mono<Void> doFilterInternal(ServerWebExchange exchange,WebFilterChain  chain) {
        String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (jwt != null && (!jwt.startsWith("Basic") && !jwt.startsWith("Bearer"))) {
            try {
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
                }
                catch (Exception e) {
                    throw new BadCredentialsException("Invalid token received");
                }
            }
            catch (BadCredentialsException e) {
                return handleError(e,exchange);
            }
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
}
