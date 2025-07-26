/*
 * Archivo: NotesRequest.java
 * Propósito: DTO utilizado en las peticiones para actualizar el campo de notas
 * de un plan de comidas diario.
 */
package com.anymeal.backend.dto;

import lombok.Data;

@Data
public class NotesRequest {
    // El nuevo contenido de texto para las notas.
    private String notes;
}