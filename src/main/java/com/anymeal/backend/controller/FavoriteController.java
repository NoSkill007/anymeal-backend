/*
 * Archivo: FavoriteController.java
 * Propósito: Este controlador maneja las operaciones relacionadas con las recetas favoritas de un usuario.
 * Permite obtener, agregar y eliminar recetas de la lista de favoritos del usuario autenticado.
 */
package com.anymeal.backend.controller;

import com.anymeal.backend.dto.FavoriteRequest;
import com.anymeal.backend.dto.RecipePreviewResponse;
import com.anymeal.backend.model.User;
import com.anymeal.backend.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @RestController: Indica que es un controlador REST.
// @RequestMapping("/api/v1/favorites"): Define la ruta base para todos los endpoints de favoritos.
// @RequiredArgsConstructor: Crea un constructor para la inyección de dependencias.
@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    // Servicio que contiene la lógica para gestionar los favoritos.
    private final FavoriteService favoriteService;

    /*
     * Endpoint para obtener la lista de recetas favoritas del usuario actual.
     * Mapeado a GET /api/v1/favorites.
     * @param user: El objeto User del usuario autenticado, inyectado por Spring Security.
     * @return Una respuesta HTTP 200 OK con la lista de previsualizaciones de recetas favoritas.
     */
    @GetMapping
    public ResponseEntity<List<RecipePreviewResponse>> getFavorites(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(favoriteService.getFavorites(user));
    }

    /*
     * Endpoint para agregar una receta a la lista de favoritos.
     * Mapeado a POST /api/v1/favorites.
     * @param user: El objeto User del usuario autenticado.
     * @param request: El cuerpo de la petición que contiene el ID de la receta a agregar.
     * @return Una respuesta HTTP 200 OK sin cuerpo, indicando éxito.
     */
    @PostMapping
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal User user, @RequestBody FavoriteRequest request) {
        favoriteService.addFavorite(user, request.recipeId());
        return ResponseEntity.ok().build();
    }

    /*
     * Endpoint para eliminar una receta de la lista de favoritos.
     * Mapeado a DELETE /api/v1/favorites/{recipeId}.
     * @param user: El objeto User del usuario autenticado.
     * @param recipeId: El ID de la receta a eliminar, extraído de la ruta.
     * @return Una respuesta HTTP 200 OK sin cuerpo, indicando éxito.
     */
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal User user, @PathVariable Long recipeId) {
        favoriteService.removeFavorite(user, recipeId);
        return ResponseEntity.ok().build();
    }
}