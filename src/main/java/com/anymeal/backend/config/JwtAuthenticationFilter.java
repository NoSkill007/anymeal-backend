/*
 * Archivo: JwtAuthenticationFilter.java
 * Propósito: Este archivo define un filtro de seguridad que se ejecuta una vez por cada petición HTTP.
 * Su función principal es interceptar la petición, buscar un token JWT en la cabecera 'Authorization',
 * validarlo y, si es válido, establecer la identidad del usuario (autenticación) en el contexto de
 * seguridad de Spring para esa petición.
 */
package com.anymeal.backend.config;

import com.anymeal.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

// Indica que esta clase es un componente de Spring y debe ser gestionada por el contenedor.
@Component
// Genera un constructor con los campos finales requeridos (inyección de dependencias).
@RequiredArgsConstructor
// La clase hereda de OncePerRequestFilter para garantizar que se ejecute solo una vez por petición.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Servicio para manejar las operaciones del token JWT (creación, validación, extracción de datos).
    private final JwtService jwtService;
    // Servicio para cargar los detalles del usuario desde la base de datos.
    private final UserDetailsService userDetailsService;

    // Método principal del filtro, que se ejecuta en cada petición.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Intenta extraer el token JWT de la cabecera de la petición.
        final String token = getTokenFromRequest(request);

        // Si no se encuentra ningún token en la petición.
        if (token == null) {
            // Se continúa con la cadena de filtros sin establecer ninguna autenticación.
            // Si el recurso es protegido, otro filtro de Spring Security denegará el acceso.
            filterChain.doFilter(request, response);
            return; // Termina la ejecución del método aquí.
        }

        // Si se encontró un token, se extrae el nombre de usuario de él.
        final String username = jwtService.getUsernameFromToken(token);

        // Se comprueba si se pudo extraer un nombre de usuario y si no hay ya una autenticación en el contexto de seguridad.
        // Esto último evita procesar el token de nuevo en peticiones ya autenticadas.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carga los detalles del usuario (roles, permisos, etc.) usando el nombre de usuario del token.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Valida el token: comprueba la firma, la fecha de expiración y si pertenece al usuario cargado.
            if (jwtService.isTokenValid(token, userDetails)) {
                // Si el token es válido, se crea un objeto de autenticación de Spring Security.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // El principal (el objeto del usuario).
                        null,        // Las credenciales (no son necesarias aquí).
                        userDetails.getAuthorities()); // Los roles y permisos del usuario.

                // Añade detalles adicionales de la petición web (como la IP) al objeto de autenticación.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establece el objeto de autenticación en el contexto de seguridad. A partir de este punto,
                // Spring Security considera que el usuario está autenticado para esta petición.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pasa la petición y la respuesta al siguiente filtro en la cadena.
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extraer el token de la cabecera 'Authorization'.
    private String getTokenFromRequest(HttpServletRequest request) {
        // Obtiene el valor de la cabecera 'Authorization'.
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Comprueba si la cabecera existe, no está vacía y comienza con "Bearer ".
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            // Devuelve el token, eliminando el prefijo "Bearer " (7 caracteres).
            return authHeader.substring(7);
        }
        // Si no cumple las condiciones, devuelve null.
        return null;
    }
}