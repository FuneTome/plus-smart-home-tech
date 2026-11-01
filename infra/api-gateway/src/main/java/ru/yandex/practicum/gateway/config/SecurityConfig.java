package ru.yandex.practicum.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/products/**", "/api/categories/**", "/api/inventory/**").permitAll()

                        .pathMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/orders/**").hasRole("USER")
                        .pathMatchers(HttpMethod.POST, "/api/orders/**").hasRole("USER")

                        .pathMatchers("/api/products/**").hasRole("ADMIN")
                        .pathMatchers("/api/categories/**").hasRole("ADMIN")
                        .pathMatchers("/api/inventory/**").hasRole("ADMIN")

                        .anyExchange().denyAll()
                )
                .cors(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(SecurityProperties properties, PasswordEncoder encoder) {
        List<UserDetails> users = properties.getUsers().stream()
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .roles(u.getRoles().toArray(new String[0]))
                        .build())
                .toList();
        return new MapReactiveUserDetailsService(users);
    }
}