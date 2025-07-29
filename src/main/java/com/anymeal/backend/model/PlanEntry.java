/*
 * Archivo: PlanEntry.java
 * Propósito: Define la entidad 'PlanEntry', mapeada a la tabla 'plan_entries'.
 * Representa una entrada específica en un plan diario, vinculando una receta a un tipo
 * de comida (ej: "Desayuno", "Almuerzo").
 */
package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan_entries")
public class PlanEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación "Muchos a Uno": Muchas entradas pueden pertenecer a un solo plan diario.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_plan_id", nullable = false)
    private DailyPlan dailyPlan;

    // Relación "Muchos a Uno": La misma receta puede ser usada en muchas entradas de plan.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    // Define el tipo de comida (ej: "Desayuno", "Almuerzo", "Cena").
    @Column(nullable = false)
    private String mealType;
}