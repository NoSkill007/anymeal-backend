/*
 * Archivo: PlanEntryDto.java
 * Propósito: Representa una única entrada en el plan de comidas, que corresponde a una receta
 * específica asignada a una comida (ej: Sopa de pollo para el almuerzo).
 */
package com.anymeal.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanEntryDto {
    // El identificador único de esta entrada del plan.
    private Long id;
    // La información de previsualización de la receta asociada a esta entrada.
    private RecipePreviewResponse recipe;
}