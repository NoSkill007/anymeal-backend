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

/**
 * Servicio encargado de todas las operaciones relacionadas con JSON Web Tokens (JWT).
 * - Generación de tokens.
 * - Validación de tokens.
 * - Extracción de información (claims) de los tokens.
 */
@Service
public class JwtService {

    // Inyecta la clave secreta desde application.properties
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * Genera un token JWT para un usuario.
     * @param userDetails Los detalles del usuario para quien se genera el token.
     * @return El token JWT como un String.
     */
    public String getToken(UserDetails userDetails) {
        return getToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT con claims (información extra) adicionales.
     * @param extraClaims Mapa con la información adicional a incluir en el token.
     * @param userDetails Los detalles del usuario.
     * @return El token JWT.
     */
    private String getToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // El "sujeto" del token es el nombre de usuario.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión del token.
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expira en 24 horas.
                .signWith(getKey(), SignatureAlgorithm.HS256) // Firma el token con la clave secreta.
                .compact();
    }

    /**
     * Obtiene la clave de firma a partir de la clave secreta en formato Base64.
     * @return La clave de firma.
     */
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrae el nombre de usuario del token.
     * @param token El token JWT.
     * @return El nombre de usuario.
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Valida si un token es correcto y pertenece a un usuario.
     * @param token El token a validar.
     * @param userDetails Los detalles del usuario contra los que se compara.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        // El token es válido si el nombre de usuario coincide y el token no ha expirado.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrae todos los claims (cuerpo) del token.
     * @param token El token JWT.
     * @return Un objeto Claims con toda la información del token.
     */
    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Función genérica para extraer un claim específico del token.
     * @param token El token JWT.
     * @param claimsResolver Una función que especifica qué claim extraer.
     * @return El valor del claim extraído.
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae la fecha de expiración del token.
     * @param token El token JWT.
     * @return La fecha de expiración.
     */
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Comprueba si un token ha expirado.
     * @param token El token JWT.
     * @return true si el token ha expirado, false si no.
     */
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}