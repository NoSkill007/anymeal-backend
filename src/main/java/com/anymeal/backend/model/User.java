package com.anymeal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad que representa la tabla 'users' en la base de datos.
 * Esta clase también implementa la interfaz UserDetails de Spring Security,
 * lo que la convierte en el objeto principal para gestionar la autenticación y autorización.
 *
 * Anotaciones de Lombok:
 * @Data: Genera automáticamente getters, setters, toString, equals y hashCode. Reduce el código repetitivo.
 * @Builder: Implementa el patrón de diseño Builder, facilitando la creación de objetos.
 * @NoArgsConstructor: Genera un constructor sin argumentos (requerido por JPA).
 * @AllArgsConstructor: Genera un constructor con todos los argumentos (útil con @Builder).
 *
 * Anotaciones de JPA (Java Persistence API):
 * @Entity: Marca esta clase como una entidad de JPA, lo que significa que se mapeará a una tabla.
 * @Table(name = "users"): Especifica el nombre de la tabla en la base de datos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}), @UniqueConstraint(columnNames = {"email"})})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // Almacenará la contraseña hasheada (codificada).

    @Enumerated(EnumType.STRING)
    private Role role;

    // --- NUEVAS RELACIONES AÑADIDAS ---

    /**
     * Relación Uno a Muchos con DailyPlan.
     * Un usuario puede tener muchos planes diarios.
     */
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<DailyPlan> dailyPlans = new ArrayList<>();

    /**
     * Relación Uno a Muchos con ShoppingListItem.
     * Un usuario puede tener muchos ítems en su lista de compras.
     */
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ShoppingListItem> shoppingListItems = new ArrayList<>();

    /**
     * Relación Muchos a Muchos con Recipe para los favoritos.
     * Un usuario puede tener muchas recetas favoritas, y una receta puede ser favorita para muchos usuarios.
     * @JoinTable: Define la tabla intermedia que gestionará esta relación.
     * @JoinColumn: Define las columnas de clave foránea en la tabla intermedia.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorite_recipes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private Set<Recipe> favoriteRecipes = new HashSet<>();


    // --- MÉTODOS DE LA INTERFAZ UserDetails ---
    // Spring Security utiliza estos métodos para manejar la seguridad.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}