/*
 * Archivo: Ingredient.java
 * Propósito: Define la entidad 'Ingredient', que representa la tabla 'ingredients'.
 * Almacena la información de un ingrediente único y reutilizable en la aplicación.
 */
package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El nombre del ingrediente debe ser único y no puede ser nulo.
    @Column(nullable = false, unique = true)
    private String name;

    // URL de una imagen representativa del ingrediente (opcional).
    private String imageUrl;
}