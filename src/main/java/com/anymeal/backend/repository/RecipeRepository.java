/*
 * Archivo: RecipeRepository.java
 * Propósito: Repositorio para la entidad Recipe. Proporciona métodos para acceder y
 * buscar recetas en la base de datos, incluyendo una consulta personalizada para búsqueda avanzada.
 */
package com.anymeal.backend.repository;

import com.anymeal.backend.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /*
     * Realiza una búsqueda de texto en varias columnas y tablas relacionadas.
     * La consulta busca el término 'query' (sin distinguir mayúsculas/minúsculas) en:
     * 1. El título de la receta (r.title).
     * 2. La categoría de la receta (r.category).
     * 3. El nombre de los ingredientes asociados (i.name), uniéndose a través de r.ingredients (ri) e ri.ingredient (i).
     * SELECT DISTINCT r: Selecciona solo recetas únicas para evitar duplicados si una receta coincide por varios criterios.
     * @param query El término de búsqueda proporcionado por el usuario.
     * @return Una lista de recetas que coinciden con la búsqueda.
     */
    @Query("SELECT DISTINCT r FROM Recipe r LEFT JOIN r.ingredients ri LEFT JOIN ri.ingredient i " +
            "WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(r.category) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Recipe> searchRecipes(@Param("query") String query);
}