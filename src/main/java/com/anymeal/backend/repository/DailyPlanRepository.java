package com.anymeal.backend.repository;

import com.anymeal.backend.model.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad DailyPlan.
 * Proporciona todas las operaciones básicas de base de datos para los planes diarios.
 */
@Repository
public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    /**
     * Busca todos los planes de un usuario específico dentro de un rango de fechas.
     * Esencial para cargar el plan de una semana completa.
     * @param userId El ID del usuario.
     * @param startDate La fecha de inicio del rango.
     * @param endDate La fecha de fin del rango.
     * @return Una lista de planes diarios para ese usuario en ese rango.
     */
    List<DailyPlan> findByUserIdAndPlanDateBetween(Long userId, LocalDate startDate, LocalDate endDate);


    /**
     * Busca un plan diario para un usuario y una fecha específicos.
     * Útil para obtener o actualizar el plan de un solo día.
     * @param userId El ID del usuario.
     * @param date La fecha específica.
     * @return Un Optional que contendrá el DailyPlan si existe.
     */
    Optional<DailyPlan> findByUserIdAndPlanDate(Long userId, LocalDate date);
}