// archivo Role.java

package com.anymeal.backend.model;

/**
 * Enum que define los roles de los usuarios en la aplicación.
 * Un Enum es una clase especial en Java que representa un grupo de constantes.
 * En este caso, un usuario puede ser un usuario normal (USER) o un administrador (ADMIN).
 * Esto permite una gestión de permisos más robusta y escalable.
 */
public enum Role {
    USER,
    ADMIN
}