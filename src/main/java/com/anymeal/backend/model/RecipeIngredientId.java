package com.anymeal.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects; // Importa Objects

/**
 * Clase que representa la clave primaria compuesta de la tabla 'recipe_ingredients'.
 *
 * @Embeddable: Indica que esta clase puede ser incrustada en otras entidades.
 * Implementa Serializable: Requisito de JPA para claves compuestas.
 */
@Data // Puedes mantener @Data, pero sobrescribiremos equals/hashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RecipeIngredientId implements Serializable {

    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "ingredient_id")
    private Long ingredientId;

    // --- ¡Implementación manual de equals y hashCode! ---
    // CRÍTICO para claves compuestas en JPA.
    // Deben basarse en los campos que forman la clave compuesta.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientId that = (RecipeIngredientId) o;
        return Objects.equals(recipeId, that.recipeId) &&
                Objects.equals(ingredientId, that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, ingredientId);
    }
}
