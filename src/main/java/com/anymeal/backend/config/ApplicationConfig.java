/*
 * Archivo: ApplicationConfig.java
 * Propósito: Este archivo de configuración de Spring se encarga de definir beans (objetos gestionados por Spring)
 * que son fundamentales para la seguridad y la autenticación en la aplicación.
 * Configura cómo se buscan los usuarios, cómo se verifican las contraseñas y cómo se gestiona la autenticación.
 */
package com.anymeal.backend.config;

import com.anymeal.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Indica que esta clase contiene configuraciones de Spring.
@Configuration
// Genera un constructor con los campos finales requeridos (inyección de dependencias).
@RequiredArgsConstructor
public class ApplicationConfig {

    // Inyección del repositorio de usuarios para acceder a la base de datos.
    private final UserRepository userRepository;

    // Define un bean llamado 'userDetailsService'. Spring Security lo usará para cargar los datos de un usuario.
    @Bean
    public UserDetailsService userDetailsService() {
        // Devuelve una implementación de UserDetailsService.
        // Ahora, se busca un usuario por su 'username' o por su 'email', usando el mismo input para ambos.
        // Esto permite a los usuarios iniciar sesión con cualquiera de los dos datos.
        return username -> userRepository.findByUsernameOrEmail(username, username)
                // Si no se encuentra el usuario, se lanza una excepción.
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Define el 'proveedor de autenticación' que utilizará Spring Security.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Se crea una instancia de DaoAuthenticationProvider, el proveedor estándar basado en DAO.
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // Se le asigna el servicio de detalles de usuario definido arriba para que sepa cómo buscar usuarios.
        authenticationProvider.setUserDetailsService(userDetailsService());
        // Se le asigna el codificador de contraseñas definido abajo para que pueda comparar las contraseñas de forma segura.
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // Define un bean para el codificador de contraseñas.
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Utiliza BCrypt, un algoritmo de hashing fuerte y estándar en la industria para almacenar contraseñas.
        return new BCryptPasswordEncoder();
    }

    // Define el 'gestor de autenticación' (AuthenticationManager) de Spring.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Obtiene y expone el AuthenticationManager configurado por defecto en Spring Security.
        return config.getAuthenticationManager();
    }
}