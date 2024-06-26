package avia.cloud.client.security;

import avia.cloud.client.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/account/**")).hasAuthority("account:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/account/**")).hasAuthority("account:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/account/**")).hasAuthority("account:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/account/**")).hasAuthority("account:delete")

                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/customer/**")).hasAuthority("customer:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/customer/**")).hasAuthority("customer:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/customer/**")).hasAuthority("customer:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/customer/**")).hasAuthority("customer:delete")

                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/airline/**")).hasAuthority("airline:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/airline/**")).hasAuthority("airline:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/airline/**")).hasAuthority("airline:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/airline/**")).hasAuthority("airline:delete")

                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/comment/**")).hasAuthority("comment:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/comment/**")).hasAuthority("comment:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/comment/**")).hasAuthority("comment:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/comment/**")).hasAuthority("comment:delete")

                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/management/**")).hasAuthority("management:create")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/management/**")).hasAuthority("management:read")
                        .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/management/**")).hasAuthority("management:update")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/management/**")).hasAuthority("management:delete")
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
