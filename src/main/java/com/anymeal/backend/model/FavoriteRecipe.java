// --- PASO 1: Entidad (Entity) para Favoritos (NUEVO Y CORREGIDO) ---
// Archivo: src/main/java/com/anymeal/backend/model/FavoriteRecipe.java
// Propósito: Define la relación entre un usuario y una receta favorita en la base de datos.
package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite_recipes")
@Data
@NoArgsConstructor
public class FavoriteRecipe {

    // Se usa una clave compuesta para la tabla pivote.
    @EmbeddedId
    private FavoriteRecipeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // Mapea la parte 'userId' de la clave compuesta.
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId") // Mapea la parte 'recipeId' de la clave compuesta.
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}