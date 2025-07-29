/*
 * Archivo: AuthService.java
 * Propósito: Este servicio encapsula la lógica de negocio para la autenticación de usuarios.
 * Se encarga de procesar las peticiones de inicio de sesión (login) y registro (register),
 * coordinando con el AuthenticationManager de Spring Security y el JwtService.
 */
package com.anymeal.backend.service;

import com.anymeal.backend.dto.AuthResponse;
import com.anymeal.backend.dto.LoginRequest;
import com.anymeal.backend.dto.RegisterRequest;
import com.anymeal.backend.model.Role;
import com.anymeal.backend.model.User;
import com.anymeal.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// @Service: Marca esta clase como un servicio de Spring, un componente de la capa de negocio.
// @RequiredArgsConstructor: Genera un constructor con todos los campos finales (inyección de dependencias).
@Service
@RequiredArgsConstructor
public class AuthService {

    // Inyección de dependencias necesarias para el servicio.
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Procesa la solicitud de inicio de sesión.
    public AuthResponse login(LoginRequest request) {
        // 1. Autentica al usuario usando el AuthenticationManager de Spring.
        //    Este gestor utilizará internamente el UserDetailsService y PasswordEncoder que hemos configurado.
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // 2. Una vez autenticado, busca al usuario para obtener sus detalles completos.
        //    Se usa el método flexible que busca por nombre de usuario o email.
        UserDetails user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(); // Lanza una excepción si no se encuentra (aunque no debería ocurrir si la autenticación fue exitosa).

        // 3. Genera un token JWT para el usuario autenticado.
        String token = jwtService.getToken(user);

        // 4. Construye y devuelve la respuesta con el token.
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    // Procesa la solicitud de registro de un nuevo usuario.
    public AuthResponse register(RegisterRequest request) {
        // 1. Crea una nueva instancia de la entidad User con los datos de la solicitud.
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                // Codifica la contraseña antes de guardarla en la base de datos.
                .password(passwordEncoder.encode(request.getPassword()))
                // Asigna el rol de usuario por defecto.
                .role(Role.USER)
                .build();

        // 2. Guarda el nuevo usuario en la base de datos.
        userRepository.save(user);

        // 3. Genera un token JWT para el nuevo usuario.
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}