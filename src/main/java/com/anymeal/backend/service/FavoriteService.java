/*
 * Archivo: FavoriteService.java
 * Propósito: Este servicio maneja toda la lógica relacionada con las recetas favoritas de un usuario,
 * incluyendo obtener, añadir y eliminar favoritos de la base de datos.
 */
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

    // Obtiene la lista de recetas favoritas de un usuario.
    @Transactional(readOnly = true) // Transacción de solo lectura para mejorar el rendimiento.
    public List<RecipePreviewResponse> getFavorites(User user) {
        // Busca todas las entradas de favoritos para el usuario.
        List<FavoriteRecipe> favoriteEntries = favoriteRecipeRepository.findByUserId(user.getId());
        // Extrae los IDs de las recetas favoritas.
        List<Long> recipeIds = favoriteEntries.stream()
                .map(favorite -> favorite.getRecipe().getId())
                .collect(Collectors.toList());
        // Si no hay favoritos, devuelve una lista vacía.
        if (recipeIds.isEmpty()) {
            return List.of();
        }
        // Busca todas las recetas correspondientes a los IDs encontrados.
        List<Recipe> recipes = recipeRepository.findAllById(recipeIds);
        // Mapea las entidades Recipe a DTOs de previsualización y las devuelve.
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

    // Añade una receta a la lista de favoritos de un usuario.
    @Transactional // Transacción de escritura.
    public void addFavorite(User user, Long recipeId) {
        // Si la receta ya es favorita, no hace nada para evitar duplicados.
        if (favoriteRecipeRepository.findByUserIdAndRecipeId(user.getId(), recipeId).isPresent()) {
            return;
        }
        // Busca la receta por su ID.
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));
        // Crea el ID compuesto para la tabla de unión.
        FavoriteRecipeId id = new FavoriteRecipeId();
        id.setUserId(user.getId());
        id.setRecipeId(recipe.getId());
        // Crea la nueva entrada de favorito y la guarda.
        FavoriteRecipe favorite = new FavoriteRecipe();
        favorite.setId(id);
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        favoriteRecipeRepository.save(favorite);
    }

    // Elimina una receta de la lista de favoritos de un usuario.
    @Transactional
    public void removeFavorite(User user, Long recipeId) {
        // Llama al método del repositorio para borrar la entrada directamente.
        favoriteRecipeRepository.deleteByUserIdAndRecipeId(user.getId(), recipeId);
    }
}