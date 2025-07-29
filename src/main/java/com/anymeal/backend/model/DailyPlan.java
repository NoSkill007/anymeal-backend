/*
 * Archivo: DailyPlan.java
 * Propósito: Define la entidad 'DailyPlan', que representa la tabla 'daily_plans' en la base de datos.
 * Esta clase almacena el plan de comidas de un usuario para una fecha específica, incluyendo notas
 * y las recetas asociadas a través de la entidad PlanEntry.
 */
package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// --- Anotaciones de Lombok ---
@Data // Genera getters, setters, toString, etc.
@Builder // Implementa el patrón de diseño Builder.
@NoArgsConstructor // Constructor sin argumentos para JPA.
@AllArgsConstructor // Constructor con todos los campos para el Builder.
// --- Anotaciones de JPA ---
@Entity // Marca esta clase como una entidad que se mapeará a una tabla.
@Table(name = "daily_plans", uniqueConstraints = {
        // Define una restricción única para que un usuario no pueda tener más de un plan para la misma fecha.
        @UniqueConstraint(columnNames = {"user_id", "plan_date"})
})
public class DailyPlan {

    // Define la clave primaria de la tabla.
    @Id
    // La base de datos generará automáticamente el valor del ID.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Define una relación "Muchos a Uno" con la entidad User.
     * Muchos planes diarios pueden pertenecer a un solo usuario.
     * fetch = FetchType.LAZY: El objeto User asociado no se cargará de la base de datos
     * hasta que sea explícitamente necesario, mejorando el rendimiento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    // Especifica la columna 'user_id' como la clave foránea que une esta tabla con la de usuarios.
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Columna para la fecha del plan. No puede ser nula.
    @Column(nullable = false)
    private LocalDate planDate;

    // @Lob (Large Object) indica que este campo puede almacenar una gran cantidad de texto.
    @Lob
    // Define el tipo de columna en la base de datos como TEXT para compatibilidad.
    @Column(columnDefinition = "TEXT")
    private String notes;

    /*
     * Define una relación "Uno a Muchos" con la entidad PlanEntry.
     * Un plan diario puede contener múltiples entradas de comida (desayuno, almuerzo, etc.).
     * mappedBy = "dailyPlan": Indica que la entidad PlanEntry es la dueña de la relación.
     * cascade = CascadeType.ALL: Cualquier cambio en DailyPlan (guardar, borrar) se propagará a sus PlanEntry asociados.
     * orphanRemoval = true: Si un PlanEntry se elimina de esta lista, también se eliminará de la base de datos.
     */
    @OneToMany(
            mappedBy = "dailyPlan",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<PlanEntry> entries = new ArrayList<>();
}