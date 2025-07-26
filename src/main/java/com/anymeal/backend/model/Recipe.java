package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa la tabla 'recipes'.
 * Contiene toda la información detallada de una receta.
 */
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

    @Column(unique = true)
    private Integer spoonacularId; // El ID original de la API de Spoonacular

    @Column(nullable = false)
    private String title;

    private String imageUrl;

    private Integer readyInMinutes;

    private Integer servings;

    @Lob // @Lob (Large Object) se usa para campos de texto largos.
    @Column(columnDefinition = "TEXT")
    private String summary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String instructions;

    private String sourceUrl;

    private String difficulty;

    private String category;

    @Column(columnDefinition = "json")
    private String diets; // Almacenado como un string JSON, ej: "[\"VEGETARIAN\", \"GLUTEN_FREE\"]"

    @Column(columnDefinition = "json")
    private String nutritionInfo; // Almacenado como un objeto JSON

    /**
     * Relación Uno a Muchos con RecipeIngredient.
     * Una receta puede tener muchos ingredientes.
     * mappedBy = "recipe": Indica que la entidad RecipeIngredient es la dueña de la relación.
     * cascade = CascadeType.ALL: Si se borra una receta, se borran sus ingredientes asociados.
     * orphanRemoval = true: Si un ingrediente se quita de la lista de una receta, se borra de la BD.
     * fetch = FetchType.LAZY: Los ingredientes no se cargarán de la BD hasta que se pidan explícitamente.
     */
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<RecipeIngredient> ingredients = new HashSet<>();

    /**
     * Relación Muchos a Muchos con User para los favoritos.
     * Se gestiona en la entidad User para mantener la lógica centralizada allí.
     */
}