/*
 * Archivo: ShoppingListItemRepository.java
 * Propósito: Repositorio para la entidad ShoppingListItem. Gestiona el acceso a los datos
 * de los artículos de la lista de compras de los usuarios.
 */
package com.anymeal.backend.repository;

import com.anymeal.backend.model.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {

    // Busca todos los artículos de la lista de compras de un usuario específico.
    List<ShoppingListItem> findByUserId(Long userId);

    // Busca un artículo específico por su ID y el ID del usuario, para asegurar que el usuario es el propietario.
    Optional<ShoppingListItem> findByIdAndUserId(Long id, Long userId);

    // Borra todos los artículos de la lista de compras de un usuario.
    @Transactional
    void deleteByUserId(Long userId);

    // Borra todos los artículos de un usuario que coincidan con el estado 'isChecked' (marcado/comprado).
    @Transactional
    void deleteByUserIdAndIsChecked(Long userId, boolean isChecked);
}