package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects; // Importa Objects

/**
 * Entidad que representa la tabla de unión 'recipe_ingredients'.
 * Conecta las recetas con sus ingredientes y especifica la cantidad y unidad.
 */
@Data // Puedes mantener @Data, pero sobrescribiremos equals/hashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    /**
     * @EmbeddedId: Marca que esta entidad usa una clave primaria compuesta,
     * definida en la clase RecipeIngredientId.
     */
    @EmbeddedId
    private RecipeIngredientId id;

    /**
     * Relación Muchos a Uno con Recipe.
     * @MapsId("recipeId"): Mapea el campo 'recipeId' de la clave compuesta (RecipeIngredientId)
     * a esta relación. Esto le dice a JPA que este campo es parte de la clave primaria.
     * FetchType.LAZY es correcto aquí.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId") // Debe coincidir con el nombre del campo en RecipeIngredientId
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    /**
     * Relación Muchos a Uno con Ingredient.
     * @MapsId("ingredientId"): Mapea el campo 'ingredientId' de la clave compuesta.
     * FetchType.LAZY es correcto aquí.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId") // Debe coincidir con el nombre del campo en RecipeIngredientId
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "unit")
    private String unit;

    @Column(name = "original_description")
    private String originalDescription;

    // --- ¡Implementación manual de equals y hashCode! ---
    // CRÍTICO para entidades con @EmbeddedId.
    // Deben basarse en el objeto @EmbeddedId.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        // La igualdad se define por la clave compuesta
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
