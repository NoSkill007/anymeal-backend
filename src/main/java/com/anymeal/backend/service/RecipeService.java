/*
 * Archivo: RecipeService.java
 * Propósito: Este servicio gestiona la lógica de negocio para las recetas. Se encarga
 * de buscarlas y de obtener sus detalles, manejando la carga de datos relacionados.
 */
package com.anymeal.backend.service;

import com.anymeal.backend.dto.RecipeDetailResponse;
import com.anymeal.backend.dto.RecipePreviewResponse;
import com.anymeal.backend.model.Recipe;
import com.anymeal.backend.model.RecipeIngredient;
import com.anymeal.backend.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    // Busca recetas, opcionalmente filtradas por un término de búsqueda.
    public List<RecipePreviewResponse> searchRecipes(String query) {
        List<Recipe> recipes;
        // Si no hay término de búsqueda, devuelve todas las recetas.
        if (query == null || query.trim().isEmpty()) {
            recipes = recipeRepository.findAll();
        } else {
            // Si hay término, usa la consulta de búsqueda personalizada.
            recipes = recipeRepository.searchRecipes(query.trim());
        }
        // Mapea los resultados a DTOs de previsualización.
        return recipes.stream()
                .map(this::mapToRecipePreviewResponse)
                .collect(Collectors.toList());
    }

    /*
     * Obtiene los detalles completos de una receta por su ID.
     * @Transactional: Es crucial aquí para mantener la sesión de la base de datos abierta,
     * lo que permite cargar colecciones con FetchType.LAZY (carga perezosa).
     */
    @Transactional(readOnly = true)
    public RecipeDetailResponse getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) {
            return null;
        }
        // Forzamos la inicialización de la colección de ingredientes, que es LAZY.
        // Esto carga los datos de la base de datos antes de que la sesión se cierre.
        Hibernate.initialize(recipe.getIngredients());
        // Mapea la entidad a un DTO de detalles y lo devuelve.
        return mapToRecipeDetailResponse(recipe);
    }

    // Mapea una entidad Recipe a un DTO de previsualización.
    private RecipePreviewResponse mapToRecipePreviewResponse(Recipe recipe) {
        return RecipePreviewResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .imageUrl(recipe.getImageUrl())
                .readyInMinutes(recipe.getReadyInMinutes() + " min")
                .difficulty(recipe.getDifficulty())
                .category(recipe.getCategory())
                .build();
    }

    // Mapea una entidad Recipe a un DTO de detalles completos.
    private RecipeDetailResponse mapToRecipeDetailResponse(Recipe recipe) {
        // Se crea una copia de la colección para operar de forma segura y evitar problemas con el proxy de Hibernate.
        List<RecipeIngredient> safeIngredients = new ArrayList<>(recipe.getIngredients());
        // Extrae las descripciones originales de los ingredientes.
        List<String> ingredients = safeIngredients.stream()
                .map(RecipeIngredient::getOriginalDescription)
                .collect(Collectors.toList());
        // Procesa el string de instrucciones para convertirlo en una lista de pasos.
        List<String> steps = (recipe.getInstructions() != null && !recipe.getInstructions().isEmpty())
                ? Arrays.asList(recipe.getInstructions().split("\\r?\\n")) // Divide por saltos de línea.
                : Collections.emptyList();
        // Construye y devuelve el DTO de respuesta.
        return RecipeDetailResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .imageUrl(recipe.getImageUrl())
                .readyInMinutes(recipe.getReadyInMinutes() + " min")
                .difficulty(recipe.getDifficulty())
                .category(recipe.getCategory())
                .description(recipe.getSummary())
                .ingredients(ingredients)
                .steps(steps)
                .build();
    }
}