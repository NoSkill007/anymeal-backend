package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa la tabla 'daily_plans'.
 * Almacena el plan de comidas de un usuario para una fecha específica.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "daily_plans", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "plan_date"})
})
public class DailyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación Muchos a Uno con User.
     * Muchos planes diarios pueden pertenecer a un solo usuario.
     * fetch = FetchType.LAZY: El usuario no se carga hasta que se necesita.
     * @JoinColumn: Especifica la columna de la clave foránea en esta tabla.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate planDate; // Usamos LocalDate para manejar solo la fecha, sin hora.

    @Lob
    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * Relación Uno a Muchos con PlanEntry.
     * Un plan diario puede tener varias entradas de comida (desayuno, almuerzo, etc.).
     */
    @OneToMany(
            mappedBy = "dailyPlan",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<PlanEntry> entries = new ArrayList<>();
}