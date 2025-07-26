// --- PASO 5: Servicio para Favoritos ---
// Archivo: src/main/java/com/anymeal/backend/service/FavoriteService.java
package com.anymeal.backend.service;

import com.anymeal.backend.dto.RecipePreviewResponse;
import com.anymeal.backend.model.FavoriteRecipe;
import com.anymeal.backend.model.FavoriteRecipeId;
import com.anymeal.backend.model.Recipe;
import com.anymeal.backend.model.User;
import com.anymeal.backend.repository.FavoriteRecipeRepository;
import com.anymeal.backend.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRecipeRepository favoriteRecipeRepository;
    private final RecipeRepository recipeRepository;

    @Transactional(readOnly = true)
    public List<RecipePreviewResponse> getFavorites(User user) {
        List<FavoriteRecipe> favoriteEntries = favoriteRecipeRepository.findByUserId(user.getId());
        List<Long> recipeIds = favoriteEntries.stream()
                .map(favorite -> favorite.getRecipe().getId())
                .collect(Collectors.toList());

        if (recipeIds.isEmpty()) {
            return List.of();
        }

        List<Recipe> recipes = recipeRepository.findAllById(recipeIds);

        return recipes.stream()
                .map(recipe -> new RecipePreviewResponse(
                        recipe.getId(),
                        recipe.getTitle(),
                        recipe.getImageUrl(),
                        recipe.getReadyInMinutes() + " min",
                        recipe.getDifficulty(),
                        recipe.getCategory()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFavorite(User user, Long recipeId) {
        if (favoriteRecipeRepository.findByUserIdAndRecipeId(user.getId(), recipeId).isPresent()) {
            return;
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        FavoriteRecipeId id = new FavoriteRecipeId();
        id.setUserId(user.getId());
        id.setRecipeId(recipe.getId());

        FavoriteRecipe favorite = new FavoriteRecipe();
        favorite.setId(id);
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        favoriteRecipeRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(User user, Long recipeId) {
        favoriteRecipeRepository.deleteByUserIdAndRecipeId(user.getId(), recipeId);
    }
}