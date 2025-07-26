// --- PASO 4: Repositorio para Favoritos ---
// Archivo: src/main/java/com/anymeal/backend/repository/FavoriteRecipeRepository.java
package com.anymeal.backend.repository;

import com.anymeal.backend.model.FavoriteRecipe;
import com.anymeal.backend.model.FavoriteRecipeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRecipeRepository extends JpaRepository<FavoriteRecipe, FavoriteRecipeId> {

    List<FavoriteRecipe> findByUserId(Long userId);

    Optional<FavoriteRecipe> findByUserIdAndRecipeId(Long userId, Long recipeId);

    @Transactional
    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);
}