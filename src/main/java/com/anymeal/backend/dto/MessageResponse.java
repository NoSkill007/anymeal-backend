/*
 * Archivo: MessageResponse.java
 * Propósito: Un DTO genérico y simple para enviar mensajes de texto como respuesta
 * desde el servidor al cliente. Útil para confirmaciones o mensajes de error.
 */
package com.anymeal.backend.dto;

public record MessageResponse(
        // El contenido del mensaje a enviar.
        String message
) {}