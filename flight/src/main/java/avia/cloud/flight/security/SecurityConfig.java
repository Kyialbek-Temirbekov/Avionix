package avia.cloud.flight.security;

import avia.cloud.flight.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationFilter authenticationFilter;
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc) throws Exception {
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/article/**")).hasAuthority("article:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/article/**")).hasAuthority("article:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/article/**")).hasAuthority("article:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/article/**")).hasAuthority("article:delete")

                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/city/**")).hasAuthority("city:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/city/**")).hasAuthority("city:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/city/**")).hasAuthority("city:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/city/**")).hasAuthority("city:delete")

                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/trip/**")).hasAuthority("flight:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/trip/**")).hasAuthority("flight:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/trip/**")).hasAuthority("flight:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/trip/**")).hasAuthority("flight:delete")

                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/ticket/**")).hasAuthority("ticket:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/ticket/**")).hasAuthority("ticket:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/ticket/**")).hasAuthority("ticket:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/ticket/**")).hasAuthority("ticket:delete")
                        .anyRequest().permitAll())
                .addFilterAfter(authenticationFilter, BasicAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
        return httpSecurity.build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
