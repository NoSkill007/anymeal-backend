/*
 * Archivo: ChangePasswordRequest.java
 * Propósito: Este DTO, definido como un 'record' de Java, estructura la petición
 * para cambiar la contraseña de un usuario.
 */
package com.anymeal.backend.dto;

// Un 'record' es una clase inmutable ideal para DTOs simples.
public record ChangePasswordRequest(
        // La contraseña actual del usuario para verificación.
        String oldPassword,
        // La nueva contraseña deseada.
        String newPassword,
        // La confirmación de la nueva contraseña para evitar errores de tipeo.
        String confirmPassword
) {}