/*
 * Archivo: RecipeIngredientId.java
 * Propósito: Define la clave primaria compuesta para la entidad RecipeIngredient.
 * Es una clase incrustable (@Embeddable) que contiene los campos que forman la clave única.
 * Debe implementar Serializable como requisito de JPA.
 */
package com.anymeal.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RecipeIngredientId implements Serializable {

    // El ID de la receta, parte de la clave compuesta.
    @Column(name = "recipe_id")
    private Long recipeId;

    // El ID del ingrediente, parte de la clave compuesta.
    @Column(name = "ingredient_id")
    private Long ingredientId;

    /*
     * Es fundamental implementar equals() y hashCode() basándose en los campos que
     * componen la clave para que JPA pueda manejar correctamente la identidad de los objetos.
     */
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