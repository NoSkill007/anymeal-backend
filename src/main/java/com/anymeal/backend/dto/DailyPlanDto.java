/*
 * Archivo: DailyPlanDto.java
 * Propósito: Este DTO representa la información de un plan de comidas para un solo día.
 * Se utiliza como parte de la respuesta del plan semanal (PlanResponse).
 */
package com.anymeal.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DailyPlanDto {
    // El identificador único del plan diario en la base de datos.
    private Long id;
    // La fecha específica a la que corresponde este plan.
    private LocalDate planDate;
    // Notas generales o comentarios para este día.
    private String notes;
    /*
     * Un mapa que organiza las comidas del día.
     * La clave (String) es el tipo de comida (ej: "Desayuno", "Almuerzo", "Cena").
     * El valor (List<PlanEntryDto>) es la lista de recetas para ese tipo de comida.
     */
    private Map<String, List<PlanEntryDto>> meals;
}