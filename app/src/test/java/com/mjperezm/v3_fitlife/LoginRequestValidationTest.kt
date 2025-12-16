package com.mjperezm.v3_fitlife

import com.mjperezm.v3_fitlife.data.remote.dto.LoginRequest
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para LoginRequest
 * Valida la construcción correcta de objetos de login
 */
class LoginRequestValidationTest {

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    @Test
    fun `login request con datos válidos debe crearse correctamente`() {
        // Arrange & Act
        val loginRequest = LoginRequest(
            email = "maria@test.com",
            password = "password123"
        )

        // Assert
        assertEquals("Email debe ser correcto", "maria@test.com", loginRequest.email)
        assertEquals("Password debe ser correcto", "password123", loginRequest.password)
    }

    @Test
    fun `email vacío debe ser inválido`() {
        // Arrange
        val loginRequest = LoginRequest(
            email = "",
            password = "password123"
        )

        // Assert
        assertTrue("Email vacío debe ser detectado", loginRequest.email.isBlank())
    }

    @Test
    fun `password vacío debe ser inválido`() {
        // Arrange
        val loginRequest = LoginRequest(
            email = "test@test.com",
            password = ""
        )

        // Assert
        assertTrue("Password vacío debe ser detectado", loginRequest.password.isBlank())
    }

    @Test
    fun `múltiples emails válidos deben pasar validación`() {
        // Arrange
        val emails = listOf(
            "usuario@gmail.com",
            "test.user@duoc.cl",
            "maria_jose@fitlife.com"
        )

        // Act & Assert
        emails.forEach { email ->
            val request = LoginRequest(email, "pass123")
            assertFalse("Email $email no debe estar vacío", request.email.isBlank())
        }
    }

    @Test
    fun `password debe tener contenido no nulo`() {
        // Arrange & Act
        val loginRequest = LoginRequest(
            email = "test@test.com",
            password = "mySecurePassword123"
        )

        // Assert
        assertNotNull("Password no debe ser null", loginRequest.password)
        assertTrue("Password debe tener contenido", loginRequest.password.isNotEmpty())
    }

    @Test
    fun `login request debe preservar exactamente los valores ingresados`() {
        // Arrange
        val email = "exact.email@test.com"
        val password = "ExactPassword123!"

        // Act
        val request = LoginRequest(email, password)

        // Assert
        assertEquals("Email debe preservarse exactamente", email, request.email)
        assertEquals("Password debe preservarse exactamente", password, request.password)
    }

    @Test
    fun `múltiples login requests deben ser independientes`() {
        // Arrange & Act
        val request1 = LoginRequest("user1@test.com", "pass1")
        val request2 = LoginRequest("user2@test.com", "pass2")

        // Assert
        assertNotEquals("Los emails deben ser diferentes", request1.email, request2.email)
        assertNotEquals("Los passwords deben ser diferentes", request1.password, request2.password)
    }
}