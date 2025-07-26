package com.anymeal.backend.repository;

import com.anymeal.backend.model.PlanEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlanEntryRepository extends JpaRepository<PlanEntry, Long> {
    // Spring Data JPA crea este método automáticamente. No necesitas implementarlo.
    // Busca todas las entradas de plan cuyos IDs de plan diario estén en la lista proporcionada.
    List<PlanEntry> findByDailyPlanIdIn(List<Long> dailyPlanIds);
}