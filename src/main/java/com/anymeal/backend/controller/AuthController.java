/*
 * Archivo: AuthController.java
 * Propósito: Este controlador gestiona los puntos de acceso (endpoints) públicos para la autenticación.
 * Se encarga de procesar las solicitudes de inicio de sesión (login) y registro (register) de usuarios.
 * No requiere que el usuario esté autenticado previamente.
 */
package com.anymeal.backend.controller;

import com.anymeal.backend.dto.AuthResponse;
import com.anymeal.backend.dto.LoginRequest;
import com.anymeal.backend.dto.RegisterRequest;
import com.anymeal.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController: Marca esta clase como un controlador REST, donde cada método devuelve un objeto que se convierte a JSON.
// @RequestMapping("/auth"): Asigna la ruta base "/auth" a todos los endpoints de este controlador.
// @RequiredArgsConstructor: Genera un constructor con los campos finales (inyección de dependencias).
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // Inyección del servicio de autenticación que contiene la lógica de negocio.
    private final AuthService authService;

    /*
     * Endpoint para procesar el inicio de sesión de un usuario.
     * Mapeado a la ruta POST /auth/login.
     * @param request: Objeto con las credenciales del usuario (username, password) recibido en el cuerpo de la petición.
     * @return Una respuesta HTTP 200 OK con el token de autenticación (JWT) en el cuerpo.
     */
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Llama al servicio para procesar el login y devuelve la respuesta generada.
        return ResponseEntity.ok(authService.login(request));
    }

    /*
     * Endpoint para registrar un nuevo usuario en el sistema.
     * Mapeado a la ruta POST /auth/register.
     * @param request: Objeto con los datos del nuevo usuario (username, email, password, etc.) recibido en el cuerpo de la petición.
     * @return Una respuesta HTTP 200 OK con el token de autenticación (JWT) para el nuevo usuario.
     */
    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Llama al servicio para registrar al usuario y devuelve la respuesta con el token.
        return ResponseEntity.ok(authService.register(request));
    }
}