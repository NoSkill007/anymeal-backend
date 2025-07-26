package com.anymeal.backend.repository;

import com.anymeal.backend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Ingredient.
 * Proporciona todas las operaciones básicas de base de datos para los ingredientes.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // Podríamos añadir búsquedas personalizadas si fuera necesario, como:
    // Optional<Ingredient> findByName(String name);
}