package avia.cloud.gatewayserver.security;

import avia.cloud.gatewayserver.filter.JWTTokenValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTTokenValidatorFilter jwtTokenValidatorFilter;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .addFilterBefore(jwtTokenValidatorFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.POST, "/avionix/client/api/customer","/avionix/client/api/customer/signIn","/avionix/client/api/airline").permitAll()
                        .pathMatchers(HttpMethod.PATCH,"/avionix/client/api/customer/confirmEmail","/avionix/client/api/airline","/avionix/client/api/airline/confirmEmail").permitAll()
                        .pathMatchers(HttpMethod.DELETE,"/avionix/client/api/customer/removeAll","/avionix/client/api/airline/removeAll").permitAll()
                        .anyExchange().authenticated()
                );
        return serverHttpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
