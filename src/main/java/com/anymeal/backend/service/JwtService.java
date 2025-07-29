/*
 * Archivo: JwtService.java
 * Propósito: Este servicio centraliza toda la funcionalidad relacionada con los JSON Web Tokens (JWT).
 * Se encarga de generar nuevos tokens para los usuarios, validar la autenticidad y vigencia
 * de los tokens recibidos, y extraer información (claims) de ellos.
 */
package com.anymeal.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Inyecta la clave secreta para firmar los tokens desde el archivo de propiedades (application.properties).
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Genera un token JWT para un usuario.
    public String getToken(UserDetails userDetails) {
        // Llama al método sobrecargado sin claims adicionales.
        return getToken(new HashMap<>(), userDetails);
    }

    // Genera un token JWT con claims (datos) adicionales.
    private String getToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // Añade claims personalizados.
                .setSubject(userDetails.getUsername()) // Establece el "sujeto" del token (quién es).
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación.
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Fecha de expiración (24 horas).
                .signWith(getKey(), SignatureAlgorithm.HS256) // Firma el token con la clave secreta y el algoritmo HS256.
                .compact(); // Construye el string del token.
    }

    // Convierte la clave secreta (en Base64) a un objeto Key para la firma.
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extrae el nombre de usuario (el "subject") de un token.
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Valida si un token es válido para un usuario específico.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        // El token es válido si el nombre de usuario coincide y el token no ha expirado.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Parsea el token y extrae todos sus claims (el cuerpo del token).
    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey()) // Usa la misma clave para verificar la firma.
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Función genérica para extraer un claim específico usando una función resolver.
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrae la fecha de expiración de un token.
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    // Comprueba si un token ha expirado comparando su fecha de expiración con la fecha actual.
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}