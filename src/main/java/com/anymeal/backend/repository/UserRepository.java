/*
 * Archivo: UserRepository.java
 * Propósito: Repositorio para la entidad User. Es fundamental para la seguridad y la gestión de usuarios,
 * ya que proporciona los métodos para buscar usuarios en la base de datos por sus credenciales.
 */
package com.anymeal.backend.repository;

import com.anymeal.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Busca un usuario por su nombre de usuario. Usado por Spring Security.
    Optional<User> findByUsername(String username);

    // Busca un usuario por su nombre de usuario o su correo electrónico. Permite iniciar sesión con cualquiera de los dos.
    Optional<User> findByUsernameOrEmail(String username, String email);

    /*
     * Comprueba si ya existe un usuario con el correo electrónico proporcionado.
     * Es útil para la validación durante el registro de nuevos usuarios.
     * @return 'true' si el email ya está en uso, 'false' en caso contrario.
     */
    Boolean existsByEmail(String email);
}