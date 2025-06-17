package com.techcompany.fastporte.users.infrastructure.auth.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final RequestFilter requestFilter;

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth",
            "/api/v1/drivers/**",
            "/api/v1/drivers/experience",
            "/api/v1/clients/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
    };

    public SecurityConfig(RequestFilter requestFilter) {
        this.requestFilter = requestFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1) Deshabilitamos CSRF porque usamos tokens/JWT
        http.csrf(AbstractHttpConfigurer::disable);

        // 2) Habilitamos CORS. Spring Security buscará un bean CorsConfigurationSource
        http.cors(Customizer.withDefaults());

        // 3) Configuración de autorización
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .anyRequest().authenticated()
        );

        // 4) Políticas de sesión (stateless porque usamos JWT)
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 5) Insertamos nuestro filtro JWT antes del filtro de autenticación estándar
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 6) Este bean define la política CORS que usarán todas las rutas /api/**
     *    (incluidas las peticiones preflight OPTIONS).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // A) Orígenes permitidos (en local, http://localhost:3000; en producción: https://fastporte.netlify.app/)
        corsConfig.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:8080",
                "https://fastporte.netlify.app"
                ));

        // B) Métodos HTTP que permitimos: todos
        corsConfig.setAllowedMethods(Arrays.asList("*"));

        // C) Headers que permitimos recibir del cliente
        corsConfig.setAllowedHeaders(Arrays.asList("*"));

        // D) Habilitar el envío de credenciales (cookies, Authorization headers, etc.)
        corsConfig.setAllowCredentials(true);

        // Exponer algún header concreto al cliente, hacerlo con:
        // corsConfig.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Asociamos esta configuración a todas las rutas que empiecen con /api/
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", corsConfig);

        return source;
    }
}
