// --- PASO 3: Repositorios ---
// Archivo NUEVO: src/main/java/com/anymeal/backend/repository/ShoppingListItemRepository.java
package com.anymeal.backend.repository;

import com.anymeal.backend.model.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    List<ShoppingListItem> findByUserId(Long userId);
    Optional<ShoppingListItem> findByIdAndUserId(Long id, Long userId);
    @Transactional void deleteByUserId(Long userId);
    @Transactional void deleteByUserIdAndIsChecked(Long userId, boolean isChecked);
}