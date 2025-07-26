/*
 * Archivo: ShoppingListDtos.java
 * Propósito: Este archivo agrupa varios DTOs (definidos como 'records') relacionados con la
 * funcionalidad de la lista de compras, manteniendo el código organizado y cohesivo.
 */
package com.anymeal.backend.dto;

import java.util.List;
import java.util.Map;

public class ShoppingListDtos {

    // Representa un único artículo en la lista de compras.
    public record ShoppingItemDto(Long id, String name, Double amount, String unit, String category, boolean isChecked) {}

    // Representa la respuesta completa de la lista de compras, con artículos agrupados por categoría.
    public record ShoppingListResponse(Map<String, List<ShoppingItemDto>> itemsByCategory) {}

    // DTO para la petición de generar una lista de compras a partir de un rango de fechas del plan.
    public record GenerateListRequest(String startDate, String endDate) {}

    // DTO para la petición de añadir un nuevo artículo personalizado a la lista.
    public record AddItemRequest(String customName, Double amount, String unit, String category) {}

    // DTO para la petición de actualizar un artículo, principalmente para marcarlo como comprado.
    public record UpdateItemRequest(boolean isChecked) {}
}