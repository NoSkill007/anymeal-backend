// --- PASO 2: Clave Compuesta para la Entidad (NUEVO) ---
// Archivo: src/main/java/com/anymeal/backend/model/FavoriteRecipeId.java
// Propósito: Define la clave primaria compuesta (userId, recipeId) para la tabla favorite_recipes.
package com.anymeal.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable
@Data
public class FavoriteRecipeId implements Serializable {
    private Long userId;
    private Long recipeId;
}