// --- PASO 2: Repositorio de Usuario (UserRepository) ACTUALIZADO ---
// Archivo: src/main/java/com/anymeal/backend/repository/UserRepository.java
// Propósito: Se utiliza tu repositorio y se le añade el método necesario.
package com.anymeal.backend.repository;

import com.anymeal.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tu método existente se mantiene intacto.
    Optional<User> findByUsername(String username);

    // Tu otro método existente también se mantiene.
    Optional<User> findByUsernameOrEmail(String username, String email);

    // --- CORRECCIÓN: Se añade el método que faltaba ---
    // Esto es necesario para que el UserService pueda comprobar si un email ya existe.
    // No romperá nada, Spring Data JPA lo implementa automáticamente.
    Boolean existsByEmail(String email);
}