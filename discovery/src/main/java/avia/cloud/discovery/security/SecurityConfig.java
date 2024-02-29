package avia.cloud.discovery.security;

import avia.cloud.discovery.filter.IdTokenReceiverFilter;
import avia.cloud.discovery.filter.JWTTokenReceiverFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private final IdTokenReceiverFilter idTokenReceiverFilter;
    private final JWTTokenReceiverFilter jwtTokenReceiverFilter;
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
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/faq/**")).hasAuthority("faq:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/faq/**")).hasAuthority("faq:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/faq/**")).hasAuthority("faq:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/faq/**")).hasAuthority("faq:delete")

                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/whyUs/**")).hasAuthority("skyline_benefits:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/whyUs/**")).hasAuthority("skyline_benefits:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/whyUs/**")).hasAuthority("skyline_benefits:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/whyUs/**")).hasAuthority("skyline_benefits:delete")
                        .anyRequest().permitAll())
                .addFilterBefore(jwtTokenReceiverFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(idTokenReceiverFilter, BasicAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return httpSecurity.build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
