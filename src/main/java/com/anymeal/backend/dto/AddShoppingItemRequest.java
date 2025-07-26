/*
 * Archivo: AddShoppingItemRequest.java
 * Propósito: Este DTO (Objeto de Transferencia de Datos) define la estructura para las solicitudes
 * de agregar un nuevo artículo a la lista de compras.
 * NOTA: Este archivo parece ser una versión anterior o alternativa a `AddItemRequest` en `ShoppingListDtos.java`.
 */
package com.anymeal.backend.dto;

import lombok.Data;

// @Data: Anotación de Lombok que genera automáticamente getters, setters, toString, etc.
@Data
public class AddShoppingItemRequest {
    // El nombre del artículo a agregar.
    private String name;
    // La cantidad del artículo (ej: "2 unidades", "500 gr").
    private String quantity;
    // La categoría a la que pertenece el artículo (ej: "Lácteos", "Frutas").
    private String category;
}