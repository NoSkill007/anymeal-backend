/*
 * Archivo: RecipeIngredient.java
 * Propósito: Define la entidad de unión 'RecipeIngredient' (tabla 'recipe_ingredients').
 * Conecta las recetas (Recipe) con los ingredientes (Ingredient) y añade información
 * adicional como la cantidad y la unidad de medida.
 */
package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    // Usa una clave primaria compuesta definida en la clase RecipeIngredientId.
    @EmbeddedId
    private RecipeIngredientId id;

    /*
     * Relación "Muchos a Uno" con Recipe.
     * @MapsId("recipeId"): Mapea el campo 'recipeId' de la clave compuesta (RecipeIngredientId) a esta relación,
     * indicando que este campo es parte de la clave primaria.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    /*
     * Relación "Muchos a Uno" con Ingredient.
     * @MapsId("ingredientId"): Mapea el campo 'ingredientId' de la clave compuesta a esta relación.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    // Cantidad del ingrediente (ej: 100).
    @Column(name = "amount")
    private Double amount;

    // Unidad de medida (ej: "gramos", "tazas").
    @Column(name = "unit")
    private String unit;

    // Descripción original del ingrediente tal como venía de la fuente de datos.
    @Column(name = "original_description")
    private String originalDescription;

    /*
     * Es crucial sobrescribir equals() y hashCode() en entidades con clave compuesta.
     * La igualdad se debe basar únicamente en la clave primaria (@EmbeddedId).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}