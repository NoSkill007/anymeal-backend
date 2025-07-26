/*
 * Archivo: UserController.java
 * Propósito: Este controlador gestiona los endpoints relacionados con el perfil del usuario autenticado.
 * Permite obtener y actualizar la información del perfil y cambiar la contraseña.
 */
package com.anymeal.backend.controller;

import com.anymeal.backend.dto.ChangePasswordRequest;
import com.anymeal.backend.dto.MessageResponse;
import com.anymeal.backend.dto.UpdateProfileRequest;
import com.anymeal.backend.dto.UserResponse;
import com.anymeal.backend.model.User;
import com.anymeal.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// Anotaciones estándar para un controlador REST de Spring.
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    // Inyección del servicio que maneja la lógica de negocio del usuario.
    private final UserService userService;

    /*
     * Endpoint para obtener la información del perfil del usuario autenticado.
     * Mapeado a GET /api/v1/user/profile.
     * @return Una respuesta HTTP 200 OK con los datos del perfil del usuario.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    /*
     * Endpoint para actualizar el perfil (nombre de usuario y/o email) del usuario.
     * Mapeado a PUT /api/v1/user/profile.
     * @param user: El objeto User del usuario autenticado.
     * @param request: El cuerpo de la petición con el nuevo username y email.
     * @return Una respuesta 200 OK con un mensaje de éxito, o 400 Bad Request con un mensaje de error.
     */
    @PutMapping("/profile")
    public ResponseEntity<MessageResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateProfileRequest request
    ) {
        try {
            userService.updateUserProfile(user, request.username(), request.email());
            // Si la actualización es exitosa, devuelve un mensaje de éxito.
            return ResponseEntity.ok(new MessageResponse("Perfil actualizado correctamente."));
        } catch (Exception e) {
            // Si el servicio lanza una excepción (ej: email ya en uso), devuelve un error 400.
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /*
     * Endpoint para cambiar la contraseña del usuario.
     * Mapeado a PUT /api/v1/user/password.
     * @param user: El objeto User del usuario autenticado.
     * @param request: El cuerpo de la petición con la contraseña antigua y la nueva (con confirmación).
     * @return Una respuesta con un mensaje de éxito o error.
     */
    @PutMapping("/password")
    public ResponseEntity<MessageResponse> changePassword(
            @AuthenticationPrincipal User user,
            @RequestBody ChangePasswordRequest request
    ) {
        try {
            userService.changePassword(user, request.oldPassword(), request.newPassword(), request.confirmPassword());
            // Si el cambio es exitoso, devuelve un mensaje de éxito.
            return ResponseEntity.ok(new MessageResponse("Contraseña cambiada correctamente."));
        } catch (IllegalArgumentException e) {
            // Captura errores de validación (ej: contraseñas no coinciden) y devuelve un error 400.
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            // Captura cualquier otro error inesperado y devuelve un error 500 del servidor.
            return ResponseEntity.status(500).body(new MessageResponse("Ocurrió un error en el servidor."));
        }
    }
}