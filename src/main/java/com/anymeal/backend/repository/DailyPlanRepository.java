/*
 * Archivo: DailyPlanRepository.java
 * Propósito: Define el repositorio para la entidad DailyPlan. Esta interfaz hereda de JpaRepository,
 * lo que le proporciona automáticamente métodos para operaciones CRUD (Crear, Leer, Actualizar, Borrar)
 * sin necesidad de escribir código. También define métodos de consulta personalizados.
 */
package com.anymeal.backend.repository;

import com.anymeal.backend.model.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// @Repository: Marca esta interfaz como un componente de repositorio de Spring.
@Repository
public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    /*
     * Busca todos los planes diarios de un usuario específico que se encuentren dentro de un rango de fechas.
     * Spring Data JPA interpreta el nombre del método para crear la consulta automáticamente.
     * @param userId El ID del usuario propietario de los planes.
     * @param startDate La fecha de inicio del rango (inclusiva).
     * @param endDate La fecha de fin del rango (inclusiva).
     * @return Una lista de objetos DailyPlan que coinciden con los criterios.
     */
    List<DailyPlan> findByUserIdAndPlanDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    /*
     * Busca un único plan diario para un usuario en una fecha específica.
     * @param userId El ID del usuario.
     * @param date La fecha exacta del plan a buscar.
     * @return Un Optional que contiene el DailyPlan si se encuentra, o un Optional vacío si no.
     */
    Optional<DailyPlan> findByUserIdAndPlanDate(Long userId, LocalDate date);
}