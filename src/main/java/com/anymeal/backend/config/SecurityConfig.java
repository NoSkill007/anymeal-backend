/*
 * Archivo: SecurityConfig.java
 * Propósito: Clase principal de configuración de seguridad de Spring. Define la cadena de filtros
 * de seguridad y las reglas de acceso (autorización) para los diferentes endpoints (URLs)
 * de la aplicación.
 */
package com.anymeal.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Indica que es una clase de configuración de Spring.
@Configuration
// Habilita la configuración de seguridad web de Spring.
@EnableWebSecurity
// Genera un constructor con los campos finales requeridos (inyección de dependencias).
@RequiredArgsConstructor
public class SecurityConfig {

    // Inyección del filtro personalizado de autenticación JWT.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // Inyección del proveedor de autenticación configurado en ApplicationConfig.
    private final AuthenticationProvider authProvider;

    // Define el bean principal de la cadena de filtros de seguridad.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Devuelve la configuración de HttpSecurity construida.
        return http
                // Deshabilita la protección CSRF (Cross-Site Request Forgery).
                // Es seguro hacerlo porque la autenticación se basa en tokens JWT, que no son vulnerables a este tipo de ataque.
                .csrf(csrf -> csrf.disable())

                // Configura las reglas de autorización para las peticiones HTTP.
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                // Permite el acceso público (sin autenticación) a todas las URLs que comiencen con "/auth/".
                                // Esto es para los endpoints de login y registro.
                                .requestMatchers("/auth/**").permitAll()

                                // Permite el acceso público a la ruta para obtener una receta específica por su ID.
                                .requestMatchers("/api/recipes/{id}").permitAll()
                                // Permite el acceso público a la ruta para obtener la lista de todas las recetas.
                                .requestMatchers("/api/recipes").permitAll()

                                // Exige que cualquier otra petición (que no coincida con las reglas anteriores) sea autenticada.
                                .anyRequest().authenticated()
                )

                // Configura la gestión de sesiones.
                .sessionManagement(sessionManager ->
                        sessionManager
                                // Establece la política de creación de sesiones como STATELESS (sin estado).
                                // Esto significa que el servidor no creará ni mantendrá una sesión HTTP.
                                // Cada petición debe ser autenticada de forma independiente usando el token JWT.
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Establece el proveedor de autenticación que debe usar Spring Security.
                .authenticationProvider(authProvider)

                // Añade nuestro filtro JWT personalizado a la cadena de filtros de Spring Security.
                // Se inserta ANTES del filtro estándar de autenticación por usuario y contraseña.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Construye y devuelve el objeto SecurityFilterChain.
                .build();
    }
}