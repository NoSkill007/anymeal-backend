/*
 * Archivo: FavoriteRecipeId.java
 * Propósito: Define una clase para la clave primaria compuesta de la entidad FavoriteRecipe.
 * Es necesaria para tablas de unión que no tienen un ID simple de una sola columna.
 * @Embeddable: Indica que esta clase puede ser incrustada como parte de otra entidad.
 */
package com.anymeal.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable
@Data
public class FavoriteRecipeId implements Serializable {
    // Parte de la clave compuesta: el ID del usuario.
    private Long userId;
    // Parte de la clave compuesta: el ID de la receta.
    private Long recipeId;
}