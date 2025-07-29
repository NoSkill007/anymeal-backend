/*
 * Archivo: RecipeIngredientRepository.java
 * Propósito: Repositorio para la entidad de unión RecipeIngredient. Facilita las consultas
 * sobre los ingredientes que componen las recetas.
 */
package com.anymeal.backend.repository;

import com.anymeal.backend.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    // Busca todos los registros de RecipeIngredient que pertenecen a una lista de IDs de recetas.
    List<RecipeIngredient> findAllByRecipeIdIn(List<Long> recipeIds);
}