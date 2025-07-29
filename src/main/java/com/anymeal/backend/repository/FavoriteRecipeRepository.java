/*
 * Archivo: FavoriteRecipeRepository.java
 * Propósito: Repositorio para la entidad de unión FavoriteRecipe. Proporciona métodos
 * para consultar y manipular los registros que marcan las recetas favoritas de los usuarios.
 */
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

    // Busca todas las entradas de recetas favoritas para un ID de usuario específico.
    List<FavoriteRecipe> findByUserId(Long userId);

    // Busca una entrada de favorito específica por el ID del usuario y el ID de la receta.
    Optional<FavoriteRecipe> findByUserIdAndRecipeId(Long userId, Long recipeId);

    /*
     * Elimina una entrada de favorito por el ID del usuario y el ID de la receta.
     * @Transactional: Asegura que la operación de borrado se ejecute dentro de una transacción.
     */
    @Transactional
    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);
}