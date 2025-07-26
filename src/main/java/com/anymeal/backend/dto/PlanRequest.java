/*
 * Archivo: PlanRequest.java
 * Propósito: Define la estructura de la petición para agregar una nueva receta al
 * planificador de comidas.
 */
package com.anymeal.backend.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PlanRequest {
    // El ID de la receta que se va a añadir al plan.
    private Long recipeId;
    // La fecha exacta para la cual se está planificando la comida.
    private LocalDate date;
    // El tipo de comida (ej: "Desayuno", "Almuerzo", "Cena").
    private String mealType;
}