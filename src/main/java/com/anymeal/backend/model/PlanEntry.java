package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la tabla 'plan_entries'.
 * Es una receta específica asignada a una comida dentro de un plan diario.
 */
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

    /**
     * Relación Muchos a Uno con DailyPlan.
     * Muchas entradas de plan pertenecen a un solo plan diario.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_plan_id", nullable = false)
    private DailyPlan dailyPlan;

    /**
     * Relación Muchos a Uno con Recipe.
     * La misma receta puede estar en muchas entradas de plan diferentes.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(nullable = false)
    private String mealType; // Ej: "Desayuno", "Almuerzo", "Cena"
}