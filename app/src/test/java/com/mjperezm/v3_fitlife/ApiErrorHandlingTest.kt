package com.mjperezm.v3_fitlife

import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para manejo de errores de API
 * Simula la lógica de interpretación de errores que usan los ViewModels
 */
class ApiErrorHandlingTest {

    /**
     * Función helper que simula el manejo de errores en los ViewModels
     */
    private fun parseApiError(exception: Exception): String {
        return when {
            exception.message?.contains("401") == true ->
                "Sesión expirada. Por favor inicia sesión nuevamente"
            exception.message?.contains("Unable to resolve host") == true ->
                "Sin conexión a internet. Verifica tu red."
            exception.message?.contains("timeout") == true ->
                "El servidor está tardando en responder. Por favor intenta de nuevo."
            exception.message?.contains("Failed to connect") == true ->
                "No se puede conectar al servidor. Verifica tu conexión."
            exception.message?.contains("409") == true ||
                    exception.message?.contains("ya está registrado") == true ->
                "Este email ya está registrado"
            else -> "Error: ${exception.localizedMessage}"
        }
    }

    @Test
    fun `error 401 debe interpretarse como sesión expirada`() {
        // Arrange
        val exception = Exception("HTTP 401 Unauthorized")

        // Act
        val mensaje = parseApiError(exception)

        // Assert
        assertTrue("Debe indicar sesión expirada",
            mensaje.contains("Sesión expirada"))
    }

    @Test
    fun `error de conexión debe interpretarse correctamente`() {
        // Arrange
        val exception = Exception("Unable to resolve host")

        // Act
        val mensaje = parseApiError(exception)

        // Assert
        assertTrue("Debe indicar problema de conexión",
            mensaje.contains("Sin conexión"))
    }

    @Test
    fun `error de timeout debe interpretarse correctamente`() {
        // Arrange
        val exception = Exception("Connection timeout")

        // Act
        val mensaje = parseApiError(exception)

        // Assert
        assertTrue("Debe indicar timeout",
            mensaje.contains("tardando en responder"))
    }

    @Test
    fun `error de servidor caído debe interpretarse correctamente`() {
        // Arrange
        val exception = Exception("Failed to connect to server")

        // Act
        val mensaje = parseApiError(exception)

        // Assert
        assertTrue("Debe indicar problema de servidor",
            mensaje.contains("No se puede conectar"))
    }

    @Test
    fun `error 409 debe interpretarse como email duplicado`() {
        // Arrange
        val exception = Exception("HTTP 409 Conflict")

        // Act
        val mensaje = parseApiError(exception)

        // Assert
        assertTrue("Debe indicar email duplicado",
            mensaje.contains("ya está registrado"))
    }

    @Test
    fun `error genérico debe mostrar mensaje del exception`() {
        // Arrange
        val exception = Exception("Error desconocido")

        // Act
        val mensaje = parseApiError(exception)

        // Assert
        assertTrue("Debe incluir el mensaje del error",
            mensaje.contains("Error desconocido"))
    }

    @Test
    fun `múltiples tipos de error deben manejarse correctamente`() {
        // Arrange
        val errores = mapOf(
            Exception("401") to "Sesión expirada",
            Exception("Unable to resolve host") to "Sin conexión",
            Exception("timeout") to "tardando",
            Exception("Failed to connect") to "No se puede conectar",
            Exception("409") to "ya está registrado"
        )

        // Act & Assert
        errores.forEach { (exception, expectedText) ->
            val mensaje = parseApiError(exception)
            assertTrue(
                "Error ${exception.message} debe contener '$expectedText'",
                mensaje.contains(expectedText, ignoreCase = true)
            )
        }
    }
}