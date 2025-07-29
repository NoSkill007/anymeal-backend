/*
 * Archivo: PlanEntryRepository.java
 * Propósito: Repositorio para la entidad PlanEntry. Permite realizar consultas sobre las
 * entradas de comidas específicas que componen los planes diarios.
 */
package com.anymeal.backend.repository;

import com.anymeal.backend.model.PlanEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlanEntryRepository extends JpaRepository<PlanEntry, Long> {

    /*
     * Busca todas las entradas de plan cuyos IDs de plan diario (dailyPlanId)
     * se encuentren en la lista de IDs proporcionada.
     * Es útil para obtener todas las comidas de varios días a la vez.
     */
    List<PlanEntry> findByDailyPlanIdIn(List<Long> dailyPlanIds);
}