package com.anymeal.backend.service;

import com.anymeal.backend.dto.RecipeDetailResponse;
import com.anymeal.backend.dto.RecipePreviewResponse;
import com.anymeal.backend.model.Recipe;
import com.anymeal.backend.model.RecipeIngredient;
import com.anymeal.backend.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate; // Importa la clase Hibernate
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importa la anotación Transactional

import java.util.ArrayList; // Importa ArrayList
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<RecipePreviewResponse> searchRecipes(String query) {
        List<Recipe> recipes;
        if (query == null || query.trim().isEmpty()) {
            recipes = recipeRepository.findAll();
        } else {
            recipes = recipeRepository.searchRecipes(query.trim());
        }

        return recipes.stream()
                .map(this::mapToRecipePreviewResponse)
                .collect(Collectors.toList());
    }

    // ¡Añade @Transactional aquí!
    // Esto asegura que la sesión de Hibernate esté abierta cuando intentemos inicializar la colección LAZY.
    @Transactional
    public RecipeDetailResponse getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) {
            return null;
        }

        // ¡Importante! Forzar la inicialización de la colección 'ingredients' aquí.
        // Esto la carga completamente de la base de datos antes de que se intente hacer stream.
        // Solo es necesario si 'ingredients' está configurado con FetchType.LAZY en la entidad Recipe.
        Hibernate.initialize(recipe.getIngredients());

        // Si tu entidad Recipe también tiene una colección de pasos (ej. List<RecipeStep>)
        // y usas LAZY, también inicialízala:
        // Hibernate.initialize(recipe.getSteps());

        return mapToRecipeDetailResponse(recipe);
    }

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

    private RecipeDetailResponse mapToRecipeDetailResponse(Recipe recipe) {
        // --- CAMBIO CLAVE AQUÍ ---
        // Primero, crea una copia de la colección de ingredientes.
        // Esto asegura que el stream opere sobre una lista independiente y no sobre la colección proxy de Hibernate.
        List<RecipeIngredient> safeIngredients = new ArrayList<>(recipe.getIngredients());

        List<String> ingredients = safeIngredients.stream() // Usa la copia segura
                .map(RecipeIngredient::getOriginalDescription)
                .collect(Collectors.toList());

        List<String> steps = (recipe.getInstructions() != null && !recipe.getInstructions().isEmpty())
                ? Arrays.asList(recipe.getInstructions().split("\\r?\\n")) // Divide los pasos por saltos de línea
                : Collections.emptyList();

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
