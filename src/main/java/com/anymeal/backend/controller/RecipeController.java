/*
 * Archivo: RecipeController.java
 * Propósito: Este controlador expone los endpoints para interactuar con las recetas.
 * Permite al público buscar recetas y ver los detalles de una receta específica.
 * El acceso a estos endpoints es público, como se configuró en SecurityConfig.
 */
package com.anymeal.backend.controller;

import com.anymeal.backend.dto.RecipeDetailResponse;
import com.anymeal.backend.dto.RecipePreviewResponse;
import com.anymeal.backend.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Anotaciones estándar para un controlador REST de Spring.
@RestController
@RequestMapping("/api/v1/recipes")
@RequiredArgsConstructor
public class RecipeController {

    // Inyección del servicio que contiene la lógica de negocio para las recetas.
    private final RecipeService recipeService;

    /*
     * Endpoint para buscar recetas. Puede recibir un término de búsqueda opcional.
     * Mapeado a GET /api/v1/recipes.
     * Si no se provee 'query', devuelve todas las recetas.
     * Si se provee (ej: /api/v1/recipes?query=sopa), filtra las recetas por ese término.
     * @param query: El término de búsqueda opcional.
     * @return Una respuesta HTTP 200 OK con una lista de previsualizaciones de recetas.
     */
    @GetMapping
    public ResponseEntity<List<RecipePreviewResponse>> searchRecipes(@RequestParam(required = false) String query) {
        List<RecipePreviewResponse> recipes = recipeService.searchRecipes(query);
        return ResponseEntity.ok(recipes);
    }

    /*
     * Endpoint para obtener los detalles completos de una receta por su ID.
     * Mapeado a GET /api/v1/recipes/{id}.
     * @param id: El ID de la receta, extraído de la ruta.
     * @return Una respuesta HTTP 200 OK con los detalles de la receta si se encuentra,
     * o una respuesta HTTP 404 Not Found si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDetailResponse> getRecipeById(@PathVariable Long id) {
        RecipeDetailResponse recipe = recipeService.getRecipeById(id);
        // Comprueba si el servicio encontró la receta.
        if (recipe != null) {
            // Si la receta existe, la devuelve en una respuesta 200 OK.
            return ResponseEntity.ok(recipe);
        } else {
            // Si la receta no existe, devuelve una respuesta 404.
            return ResponseEntity.notFound().build();
        }
    }
}