/*
 * Archivo: PlanController.java
 * Propósito: Este controlador gestiona las operaciones del planificador de comidas semanal.
 * Permite obtener el plan de una semana, agregar o eliminar recetas del plan, y actualizar notas.
 */
package com.anymeal.backend.controller;

import com.anymeal.backend.dto.NotesRequest;
import com.anymeal.backend.dto.PlanRequest;
import com.anymeal.backend.dto.PlanResponse;
import com.anymeal.backend.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

// Anotaciones estándar para un controlador REST de Spring.
@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController {

    // Inyección del servicio que maneja la lógica del planificador.
    private final PlanService planService;

    /*
     * Endpoint para obtener el plan de comidas para una semana específica.
     * Mapeado a GET /api/v1/plans.
     * @param startDate: La fecha de inicio de la semana, recibida como parámetro de la URL (ej: ?startDate=2024-12-30).
     * Debe estar en formato ISO (YYYY-MM-DD).
     * @return Una respuesta HTTP 200 OK con el objeto del plan semanal.
     */
    @GetMapping
    public ResponseEntity<PlanResponse> getWeeklyPlan(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return ResponseEntity.ok(planService.getPlanForWeek(startDate));
    }

    /*
     * Endpoint para añadir una receta a un día y tipo de comida específicos en el plan.
     * Mapeado a POST /api/v1/plans.
     * @param request: Cuerpo de la petición con los detalles de la entrada del plan (fecha, receta, tipo de comida).
     * @return Una respuesta HTTP 200 OK vacía.
     */
    @PostMapping
    public ResponseEntity<Void> addRecipeToPlan(@RequestBody PlanRequest request) {
        planService.addRecipeToPlan(request);
        return ResponseEntity.ok().build();
    }

    /*
     * Endpoint para eliminar una entrada específica (una comida) del plan.
     * Mapeado a DELETE /api/v1/plans/entries/{entryId}.
     * @param entryId: El ID de la entrada del plan a eliminar.
     * @return Una respuesta HTTP 200 OK vacía.
     */
    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<Void> deletePlanEntry(@PathVariable Long entryId) {
        planService.deletePlanEntry(entryId);
        return ResponseEntity.ok().build();
    }

    /*
     * Endpoint para actualizar las notas asociadas a un plan semanal.
     * Mapeado a PUT /api/v1/plans/{planId}/notes.
     * @param planId: El ID del plan a actualizar.
     * @param request: El cuerpo de la petición con las nuevas notas.
     * @return Una respuesta HTTP 200 OK vacía.
     */
    @PutMapping("/{planId}/notes")
    public ResponseEntity<Void> updateNotes(@PathVariable Long planId, @RequestBody NotesRequest request) {
        planService.updateNotes(planId, request);
        return ResponseEntity.ok().build();
    }
}