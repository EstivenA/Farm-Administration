package com.granja.granja_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()   // Permitir todo sin login
            )
            .csrf(csrf -> csrf.disable())   // Desactivar CSRF
            .formLogin(form -> form.disable()) // 🔑 Desactivar login por formulario
            .httpBasic(basic -> basic.disable()); // 🔑 Desactivar login básico
        return http.build();
    }
}

