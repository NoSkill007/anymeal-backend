/*
 * Archivo: RegisterRequest.java
 * Propósito: Este DTO define la estructura de datos que el cliente debe enviar
 * en el cuerpo de la petición para registrar un nuevo usuario.
 */
package com.anymeal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    // El nombre de usuario deseado para el nuevo usuario.
    private String username;
    // La dirección de correo electrónico del nuevo usuario.
    private String email;
    // La contraseña para la nueva cuenta.
    private String password;
}