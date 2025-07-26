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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        // 1. Spring Security usa nuestro UserDetailsService actualizado para validar las credenciales.
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // --- CAMBIO SUTIL PERO IMPORTANTE ---
        // 2. Buscamos al usuario de nuevo usando el método flexible para asegurarnos
        //    de que tenemos el objeto correcto para generar el token.
        UserDetails user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow();

        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}