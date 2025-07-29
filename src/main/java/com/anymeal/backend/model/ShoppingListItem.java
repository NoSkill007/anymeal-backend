// Archivo: ShoppingListItem.java
package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Entity
@Table(name = "shopping_list_items")
@Data
@NoArgsConstructor
public class ShoppingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "custom_name")
    private String customName;

    private Double amount;
    private String unit;
    private String category;

    @Column(name = "is_checked", nullable = false)
    private boolean isChecked = false;

    @Column(name = "added_at", updatable = false, insertable = false)
    private Timestamp addedAt;
}