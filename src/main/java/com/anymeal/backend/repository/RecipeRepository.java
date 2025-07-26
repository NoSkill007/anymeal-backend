package com.anymeal.backend.repository;

import com.anymeal.backend.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /**
     * --- MÉTODO NUEVO ---
     * Realiza una búsqueda de texto completo en los campos de título, categoría y
     * nombres de los ingredientes asociados.
     * @param query El término de búsqueda.
     * @return Una lista de recetas únicas que coinciden con el criterio de búsqueda.
     */
    @Query("SELECT DISTINCT r FROM Recipe r LEFT JOIN r.ingredients ri LEFT JOIN ri.ingredient i " +
            "WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(r.category) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Recipe> searchRecipes(@Param("query") String query);
}