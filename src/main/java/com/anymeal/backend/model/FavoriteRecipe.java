/*
 * Archivo: FavoriteRecipe.java
 * Propósito: Define la entidad 'FavoriteRecipe', que actúa como la tabla intermedia (pivote)
 * para la relación "Muchos a Muchos" entre User y Recipe. Cada registro en esta tabla
 * representa una receta marcada como favorita por un usuario.
 */
package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite_recipes")
@Data
@NoArgsConstructor
public class FavoriteRecipe {

    // Se utiliza una clave primaria compuesta (userId y recipeId) definida en FavoriteRecipeId.
    @EmbeddedId
    private FavoriteRecipeId id;

    // Relación "Muchos a Uno" con User.
    @ManyToOne(fetch = FetchType.LAZY)
    // Mapea la parte 'userId' de la clave compuesta a esta relación.
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    // Relación "Muchos a Uno" con Recipe.
    @ManyToOne(fetch = FetchType.LAZY)
    // Mapea la parte 'recipeId' de la clave compuesta a esta relación.
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}