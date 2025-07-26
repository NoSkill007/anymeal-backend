/*
 * Archivo: RecipePreviewResponse.java
 * Propósito: DTO que contiene una versión resumida de la información de una receta.
 * Es ideal para usar en listas o cuadrículas donde no se necesita mostrar todos los detalles.
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
public class RecipePreviewResponse {
    // ID único de la receta.
    private Long id;
    // Título o nombre de la receta.
    private String title;
    // URL de la imagen principal de la receta.
    private String imageUrl;
    // Tiempo estimado de preparación en minutos.
    private String readyInMinutes;
    // Nivel de dificultad de la receta.
    private String difficulty;
    // Categoría de la receta.
    private String category;
}