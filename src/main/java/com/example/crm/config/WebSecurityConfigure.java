package com.example.crm.config;


import com.example.crm.config.jwt.JwtAuthenticationEntryPoint;
import com.example.crm.config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure {

    private final JwtAuthenticationFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint point;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // ===== allow preflight =====
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ===== LOGIN APIs =====
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/login-with-otp",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password",
                                "/api/auth/logout",
                                "/reset-password",
                                "/reset-password/**",
                                "/forgot-password"
                        ).permitAll()

                        // ===== SWAGGER =====
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ===== ALL OTHER APIs =====
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(point)
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        jwtRequestFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}
