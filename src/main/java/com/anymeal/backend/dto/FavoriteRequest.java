/*
 * Archivo: FavoriteRequest.java
 * Propósito: Este DTO estructura la petición para añadir o quitar una receta de favoritos.
 * Solo contiene el ID de la receta implicada.
 */
package com.anymeal.backend.dto;

public record FavoriteRequest(
        // El identificador único de la receta que se va a marcar/desmarcar como favorita.
        Long recipeId
) {}