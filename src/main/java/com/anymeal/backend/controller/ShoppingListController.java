/*
 * Archivo: ShoppingListController.java
 * Propósito: Este controlador gestiona todas las operaciones relacionadas con la lista de compras del usuario.
 * Permite generar la lista a partir del plan, agregar o actualizar ítems, y limpiar los ítems ya comprados.
 */
package com.anymeal.backend.controller;

import com.anymeal.backend.dto.ShoppingListDtos.*;
import com.anymeal.backend.model.User;
import com.anymeal.backend.service.ShoppingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// Anotaciones estándar para un controlador REST de Spring.
@RestController
@RequestMapping("/api/v1/shopping-list")
@RequiredArgsConstructor
public class ShoppingListController {

    // Inyección del servicio que maneja la lógica de la lista de compras.
    private final ShoppingListService shoppingListService;

    /*
     * Endpoint para generar una lista de compras a partir del plan de comidas.
     * Mapeado a POST /api/v1/shopping-list/generate.
     * @param user: El usuario autenticado.
     * @param request: El cuerpo de la petición con el rango de fechas para generar la lista.
     * @return Una respuesta HTTP 200 OK con la lista de compras generada.
     */
    @PostMapping("/generate")
    public ResponseEntity<ShoppingListResponse> generateAndGetList(@AuthenticationPrincipal User user, @RequestBody GenerateListRequest request) {
        shoppingListService.generateListFromPlan(user, request);
        // Después de generar, obtiene la lista actualizada y la devuelve.
        return ResponseEntity.ok(shoppingListService.getShoppingListForUser(user));
    }

    /*
     * Endpoint para añadir un nuevo ítem a la lista de compras manualmente.
     * Mapeado a POST /api/v1/shopping-list.
     * @param user: El usuario autenticado.
     * @param request: El cuerpo de la petición con los detalles del ítem a añadir.
     * @return Una respuesta HTTP 200 OK con el ítem recién creado.
     */
    @PostMapping
    public ResponseEntity<ShoppingItemDto> addItem(@AuthenticationPrincipal User user, @RequestBody AddItemRequest request) {
        return ResponseEntity.ok(shoppingListService.addItem(user, request));
    }

    /*
     * Endpoint para actualizar un ítem existente en la lista (ej: marcar como comprado).
     * Mapeado a PUT /api/v1/shopping-list/{itemId}.
     * @param user: El usuario autenticado.
     * @param itemId: El ID del ítem a actualizar.
     * @param request: El cuerpo de la petición con los nuevos datos del ítem.
     * @return Una respuesta 200 OK con el ítem actualizado, o 404 si el ítem no se encuentra.
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<ShoppingItemDto> updateItem(@AuthenticationPrincipal User user, @PathVariable Long itemId, @RequestBody UpdateItemRequest request) {
        // El servicio devuelve un Optional, lo que facilita manejar el caso de 'no encontrado'.
        return shoppingListService.updateItem(user, itemId, request)
                .map(ResponseEntity::ok) // Si el Optional contiene un valor, crea una respuesta 200 OK.
                .orElse(ResponseEntity.notFound().build()); // Si está vacío, crea una respuesta 404.
    }

    /*
     * Endpoint para eliminar todos los ítems marcados como comprados de la lista.
     * Mapeado a POST /api/v1/shopping-list/clear-checked.
     * @param user: El usuario autenticado.
     * @return Una respuesta HTTP 200 OK vacía.
     */
    @PostMapping("/clear-checked")
    public ResponseEntity<Void> clearCheckedItems(@AuthenticationPrincipal User user) {
        shoppingListService.clearCheckedItems(user);
        return ResponseEntity.ok().build();
    }
}