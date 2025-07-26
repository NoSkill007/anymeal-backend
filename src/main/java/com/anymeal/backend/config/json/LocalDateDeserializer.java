/*
 * Archivo: LocalDateDeserializer.java
 * Propósito: Este archivo define un deserializador personalizado para la clase `java.time.LocalDate`.
 * Permite que la librería Jackson convierta (deserialice) una representación JSON,
 * ya sea como un string en formato ISO (ej: "2024-12-31") o como un objeto
 * (ej: {"year": 2024, "month": 12, "day": 31}), en un objeto `LocalDate` de Java.
 * También incluye registros (logs) para facilitar la depuración.
 */
package com.anymeal.backend.config.json; // Define el paquete al que pertenece la clase.

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger; // Importa la interfaz del logger para registrar eventos.
import org.slf4j.LoggerFactory; // Importa la fábrica para obtener una instancia del logger.

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException; // Importa la excepción para errores al parsear fechas.

// La clase hereda de JsonDeserializer e indica que manejará la deserialización para objetos LocalDate.
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    // Se crea una instancia estática y final del logger para esta clase.
    private static final Logger logger = LoggerFactory.getLogger(LocalDateDeserializer.class);

    // Este método es el núcleo del deserializador y es llamado por Jackson.
    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Lee el contenido JSON y lo convierte en un árbol de nodos (JsonNode) para facilitar su manipulación.
        JsonNode node = p.getCodec().readTree(p);
        // Registra el JSON recibido para propósitos de depuración.
        logger.info("Intentando deserializar LocalDate. JSON recibido: {}", node.toString());

        // Comprueba si el nodo JSON es un valor de texto (un string).
        if (node.isTextual()) {
            String dateString = node.asText(); // Obtiene el valor como un string.
            // Registra que se intentará parsear el string.
            logger.info("JSON es textual. Intentando parsear como String: {}", dateString);
            try {
                // Intenta convertir el string a un LocalDate usando el formato estándar (ISO 8601).
                return LocalDate.parse(dateString);
            } catch (DateTimeParseException e) {
                // Si ocurre un error de formato, se registra el problema.
                logger.error("Error al parsear la cadena '{}' como LocalDate: {}", dateString, e.getMessage());
                // La ejecución continuará para ver si se puede interpretar como un objeto.
            }
        }

        // Si no es un texto o si falló el parseo anterior, comprueba si es un objeto JSON.
        if (node.isObject()) {
            // Registra que se procesará como un objeto.
            logger.info("JSON es un objeto. Intentando leer campos year, month, day.");
            // Valida que los campos necesarios ('year', 'month', 'day') existan en el objeto.
            if (!node.has("year") || !node.has("month") || !node.has("day")) {
                // Si falta alguno de los campos, se registra un error.
                logger.error("Faltan campos 'year', 'month' o 'day' en el objeto JSON de LocalDate: {}", node.toString());
                // Lanza una excepción para detener el proceso y señalar el error.
                throw new IOException("Missing year, month, or day in LocalDate JSON object: " + node.toString());
            }

            // Extrae los valores numéricos de cada campo del objeto JSON.
            int year = node.get("year").asInt();
            int month = node.get("month").asInt();
            int day = node.get("day").asInt();

            // Registra los valores extraídos para depuración.
            logger.info("Campos extraídos: year={}, month={}, day={}", year, month, day);
            try {
                // Intenta crear un objeto LocalDate a partir de los componentes extraídos.
                return LocalDate.of(year, month, day);
            } catch (Exception e) {
                // Si los componentes no forman una fecha válida (ej: mes 13), se registra un error.
                logger.error("Error al crear LocalDate con year={}, month={}, day={}: {}", year, month, day, e.getMessage());
                // Lanza una excepción con el mensaje del error original.
                throw new IOException("Invalid date components: " + e.getMessage(), e);
            }
        }

        // Si el JSON no es ni un string ni un objeto compatible, se registra el error.
        logger.error("Formato de fecha no reconocido. JSON recibido: {}", node.toString());
        // Lanza una excepción final indicando que el formato no es soportado.
        throw new IOException("Unsupported date format. Expected String (YYYY-MM-DD) or Object {year, month, day}. Received: " + node.toString());
    }
}