// Archivo NUEVO: src/main/java/com/anymeal/backend/repository/RecipeIngredientRepository.java
package com.anymeal.backend.repository;

import com.anymeal.backend.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findAllByRecipeIdIn(List<Long> recipeIds);
}