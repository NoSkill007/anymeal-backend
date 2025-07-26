/*
 * Archivo: RecipeDetailResponse.java
 * Propósito: DTO para enviar al cliente toda la información detallada de una receta.
 * Se utiliza para mostrar la vista completa de una receta, incluyendo ingredientes y pasos.
 */
package com.anymeal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDetailResponse {
    // ID único de la receta.
    private Long id;
    // Título o nombre de la receta.
    private String title;
    // URL de la imagen principal de la receta.
    private String imageUrl;
    // Tiempo estimado de preparación en minutos.
    private String readyInMinutes;
    // Nivel de dificultad (ej: "Fácil", "Medio", "Difícil").
    private String difficulty;
    // Categoría de la receta (ej: "Sopas", "Postres").
    private String category;
    // Una breve descripción o introducción a la receta.
    private String description;
    // Lista de los ingredientes necesarios.
    private List<String> ingredients;
    // Lista ordenada de los pasos a seguir para la preparación.
    private List<String> steps;
}