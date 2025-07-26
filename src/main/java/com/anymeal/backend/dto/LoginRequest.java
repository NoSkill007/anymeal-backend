/*
 * Archivo: LoginRequest.java
 * Propósito: Este DTO define la estructura de datos que el cliente debe enviar
 * en el cuerpo de la petición para iniciar sesión.
 */
package com.anymeal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Anotaciones de Lombok para la generación automática de código.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    // El nombre de usuario o email del usuario que intenta iniciar sesión.
    private String username;
    // La contraseña del usuario.
    private String password;
}