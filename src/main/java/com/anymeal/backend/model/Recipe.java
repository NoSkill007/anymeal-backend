/*
 * Archivo: Recipe.java
 * Propósito: Define la entidad 'Recipe', que representa la tabla 'recipes'.
 * Contiene toda la información detallada de una receta, como título, instrucciones,
 * ingredientes y metadatos adicionales.
 */
package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Guarda el ID original de la API externa (Spoonacular) para referencia.
    @Column(unique = true)
    private Integer spoonacularId;

    @Column(nullable = false)
    private String title;

    private String imageUrl;
    private Integer readyInMinutes;
    private Integer servings;

    // @Lob indica un objeto grande, adecuado para textos largos.
    @Lob
    @Column(columnDefinition = "TEXT")
    private String summary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String instructions;

    private String sourceUrl;
    private String difficulty;
    private String category;

    // Almacena información sobre dietas como un string en formato JSON.
    @Column(columnDefinition = "json")
    private String diets;

    // Almacena información nutricional como un string en formato JSON.
    @Column(columnDefinition = "json")
    private String nutritionInfo;

    /*
     * Relación "Uno a Muchos" con RecipeIngredient.
     * Una receta se compone de muchos ingredientes (con sus cantidades).
     * mappedBy="recipe": La entidad RecipeIngredient gestiona la relación.
     * cascade=CascadeType.ALL: Las operaciones sobre Recipe se propagan a sus ingredientes.
     * orphanRemoval=true: Si un RecipeIngredient se elimina de este Set, se borra de la BD.
     */
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<RecipeIngredient> ingredients = new HashSet<>();
}