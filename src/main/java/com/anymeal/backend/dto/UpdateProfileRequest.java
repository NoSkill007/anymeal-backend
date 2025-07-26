/*
 * Archivo: UpdateProfileRequest.java
 * Propósito: DTO que estructura la petición para actualizar los datos del perfil
 * de un usuario, como su nombre de usuario o email.
 */
package com.anymeal.backend.dto;

public record UpdateProfileRequest(
        // El nuevo nombre de usuario.
        String username,
        // El nuevo correo electrónico.
        String email
) {}