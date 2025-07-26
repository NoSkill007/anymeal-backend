/*
 * Archivo: AuthResponse.java
 * Propósito: Este DTO representa la respuesta que se envía al cliente tras una autenticación
 * (login o registro) exitosa. Su principal contenido es el token de acceso.
 */
package com.anymeal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Anotaciones de Lombok para generar código repetitivo.
@Data // Getters, setters, etc.
@Builder // Patrón de diseño Builder para construir objetos.
@NoArgsConstructor // Constructor sin argumentos.
@AllArgsConstructor // Constructor con todos los argumentos.
public class AuthResponse {
    /*
     * El token de acceso JWT (JSON Web Token).
     * El cliente debe almacenar este token de forma segura y enviarlo en la cabecera
     * 'Authorization' de las futuras peticiones a endpoints protegidos.
     */
    private String token;
}