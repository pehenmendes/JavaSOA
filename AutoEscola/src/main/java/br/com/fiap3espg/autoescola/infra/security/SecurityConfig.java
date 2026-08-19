package br.com.fiap3espg.autoescola.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Decisão de gerenciar autorizações pelo controller
@EnableMethodSecurity // Decisão de gerenciar autorizações pelo controller
@RequiredArgsConstructor
public class SecurityConfig {
    private final SecurityFilter secFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .csrf(csfr -> csfr.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/login").permitAll()
//                              Opção de gerenciamento de autorizações pelo config (mais profissional):
//                                .requestMatchers(HttpMethod.POST, "/instrutores").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/instrutores").hasAnyRole("ADMIN","USER")
//                                .requestMatchers(HttpMethod.GET, "/instrutores/{id}").hasRole("ADMIN")
//                                .requestMatchers("/instrutores").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(secFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
