/*
 * Archivo: IngredientRepository.java
 * Propósito: Repositorio para la entidad Ingredient. Provee las operaciones CRUD básicas
 * para gestionar los ingredientes en la base de datos a través de JpaRepository.
 */
package com.anymeal.backend.repository;

import com.anymeal.backend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // Spring Data JPA proporciona métodos como save(), findById(), findAll(), deleteById(), etc.
    // Se podrían añadir métodos de consulta personalizados si fueran necesarios, por ejemplo:
    // Optional<Ingredient> findByName(String name);
}