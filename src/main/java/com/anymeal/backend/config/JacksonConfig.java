/*
 * Archivo: JacksonConfig.java
 * Propósito: Este archivo de configuración de Spring personaliza el comportamiento de la librería Jackson,
 * que es la encargada de convertir objetos Java a JSON (serialización) y viceversa (deserialización).
 * Principalmente, registra el deserializador personalizado para `LocalDate`.
 */
package com.anymeal.backend.config;

import com.anymeal.backend.config.json.LocalDateDeserializer; // Importa el deserializador personalizado.
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule; // Importa el módulo simple para registrar componentes personalizados.
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Importa el módulo para soportar los tipos de fecha y hora de Java 8.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

// Indica que esta clase contiene configuraciones de Spring.
@Configuration
public class JacksonConfig {

    // Define un bean de tipo ObjectMapper, que será el objeto principal de Jackson usado en la aplicación.
    @Bean
    public ObjectMapper objectMapper() {
        // Crea una nueva instancia de ObjectMapper.
        ObjectMapper objectMapper = new ObjectMapper();

        // Registra el JavaTimeModule. Esto le enseña a Jackson cómo manejar los tipos de `java.time`
        // (como LocalDate, LocalDateTime), serializándolos por defecto a un formato de string estándar (ISO 8601).
        objectMapper.registerModule(new JavaTimeModule());

        // Deshabilita la característica de escribir fechas como timestamps numéricos (ej: 1672531200).
        // En su lugar, siempre usará el formato de string ISO ("2023-01-01T00:00:00").
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // --- REGISTRO DEL DESERIALIZADOR PERSONALIZADO ---
        // Crea un módulo personalizado para añadir funcionalidades específicas.
        SimpleModule customDateModule = new SimpleModule();
        // Asocia la clase LocalDate con nuestro deserializador personalizado (LocalDateDeserializer).
        // Ahora, cada vez que Jackson necesite convertir un JSON a un LocalDate, usará nuestra lógica.
        customDateModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        // Registra el módulo personalizado en el ObjectMapper.
        objectMapper.registerModule(customDateModule);

        // Devuelve el ObjectMapper completamente configurado para que Spring lo utilice.
        return objectMapper;
    }
}