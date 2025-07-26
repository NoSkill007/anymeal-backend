/*
 * Archivo: PlanResponse.java
 * Propósito: DTO que representa la respuesta completa del plan semanal. Envía al cliente
 * todos los planes diarios para una semana, organizados por fecha.
 */
package com.anymeal.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class PlanResponse {
    /*
     * Un mapa que contiene los planes de cada día.
     * La clave (String) es la fecha en formato de texto (ej: "2024-12-31").
     * El valor (DailyPlanDto) es el objeto que contiene los detalles del plan para ese día.
     */
    private Map<String, DailyPlanDto> dailyPlans;
}