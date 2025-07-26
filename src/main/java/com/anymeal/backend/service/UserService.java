// --- PASO 4: Servicio de Usuario (UserService) ---
// Archivo: src/main/java/com/anymeal/backend/service/UserService.java
package com.anymeal.backend.service;

import com.anymeal.backend.dto.UserResponse;
import com.anymeal.backend.model.User;
import com.anymeal.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public void updateUserProfile(User user, String newUsername, String newEmail) {
        User userToUpdate = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (!userToUpdate.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("El email ya está en uso.");
        }

        userToUpdate.setUsername(newUsername);
        userToUpdate.setEmail(newEmail);
        userRepository.save(userToUpdate);
    }

    @Transactional
    public void changePassword(User user, String oldPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Las nuevas contraseñas no coinciden.");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 6 caracteres.");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        User userToUpdate = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        userToUpdate.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToUpdate);
    }
}