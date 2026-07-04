package com.digitaldentic.sistemaodontologico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.digitaldentic.sistemaodontologico.security.LoginSuccessHandler;

@Configuration
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;

    public SecurityConfig(LoginSuccessHandler loginSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http

            // ✅ CSRF ACTIVO
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/cita/guardar")
            )

            .authorizeHttpRequests(auth -> auth

                // 🌍 PUBLICO
                .requestMatchers(
                        "/", 
                        "/login",
                        "/css/**", 
                        "/js/**", 
                        "/img/**"
                ).permitAll()

                // 🌍 RESERVA PUBLICA
                .requestMatchers("/cita/guardar").permitAll()
                .requestMatchers("/cita/confirmacion").permitAll()

                // 🔒 CREAR CITA
                .requestMatchers("/cita/reservar")
                    .hasAnyRole("ADMIN","SECRETARIO","DOCTOR")

                // 🔒 CITAS
                .requestMatchers("/cita/lista")
                    .hasAnyRole("ADMIN","SECRETARIO","DOCTOR")

                .requestMatchers("/cita/mis-citas")
                    .hasRole("DOCTOR")

                .requestMatchers("/cita/actualizar-estado")
                    .hasAnyRole("ADMIN","SECRETARIO","DOCTOR")

                // 🔒 PACIENTES
                .requestMatchers("/pacientes/**")
                    .hasAnyRole("ADMIN","SECRETARIO","DOCTOR")

                // 🔥 HISTORIAL CLINICO (AQUI ESTA LA SOLUCION)
                .requestMatchers("/historial/**")
                    .hasAnyRole("ADMIN","DOCTOR")

                // 🔒 AREAS POR ROL
                .requestMatchers("/admin/**")
                    .hasRole("ADMIN")

                .requestMatchers("/doctor/**")
                    .hasAnyRole("ADMIN","DOCTOR")

                .requestMatchers("/secretario/**")
                    .hasAnyRole("ADMIN","SECRETARIO")

   
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(loginSuccessHandler)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .permitAll()
            );

        return http.build();
    }
}