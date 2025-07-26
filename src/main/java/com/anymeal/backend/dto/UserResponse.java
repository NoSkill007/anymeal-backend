/*
 * Archivo: UserResponse.java
 * Propósito: DTO que define la estructura de los datos del perfil de un usuario que se
 * envían como respuesta desde el servidor. Excluye información sensible como la contraseña.
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
public class UserResponse {
    // El identificador único del usuario.
    private Long id;
    // El nombre de usuario.
    private String username;
    // La dirección de correo electrónico del usuario.
    private String email;
}