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
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Stream;

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
        if (Stream.of("/api/customer/confirmEmail","/api/customer/signIn")
                .noneMatch(path -> exchange.getRequest().getPath().toString().equals(path)) &&
                (!exchange.getRequest().getMethod().toString().equals("POST") && exchange.getRequest().getPath().toString().equals("/api/customer"))) {
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
            }
            catch (Exception e) {
                ErrorResponseDTO responseBody = new ErrorResponseDTO(
                        exchange.getRequest().getPath().toString(),
                        HttpStatus.UNAUTHORIZED,
                        "Invalid token received",
                        LocalDateTime.now().toString()
                );
                ServerHttpResponse serverHttpResponse = exchange.getResponse();
                serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return serverHttpResponse.writeWith(Mono.just(serverHttpResponse.bufferFactory().wrap(objectMapper.writeValueAsBytes(responseBody))));
            }
        }

        return chain.filter(exchange);
    }
}
