package com.mjperezm.v3_fitlife

import com.mjperezm.v3_fitlife.data.remote.dto.SignupRequest
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para SignupRequest
 * Valida la construcción correcta de objetos de registro de usuario
 * y las validaciones de campos
 */
class SignupRequestValidationTest {

    // Helper functions para validación (simulan lógica que debería estar en un validador)
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun isValidPhone(phone: String?): Boolean {
        if (phone == null) return true // Teléfono es opcional
        return phone.matches("^\\+?[0-9]{9,15}$".toRegex())
    }

    @Test
    fun `signup request con datos válidos debe crearse correctamente`() {
        // Arrange & Act
        val signupRequest = SignupRequest(
            email = "maria@test.com",
            password = "password123",
            role = "USUARIO",
            nombre = "María José",
            telefono = "+56912345678"
        )

        // Assert
        assertEquals("Email debe ser correcto", "maria@test.com", signupRequest.email)
        assertEquals("Password debe ser correcto", "password123", signupRequest.password)
        assertEquals("Role debe ser USUARIO", "USUARIO", signupRequest.role)
        assertEquals("Nombre debe ser correcto", "María José", signupRequest.nombre)
        assertEquals("Teléfono debe ser correcto", "+56912345678", signupRequest.telefono)
    }

    @Test
    fun `signup request con rol por defecto debe ser USUARIO`() {
        // Arrange & Act
        val signupRequest = SignupRequest(
            email = "test@test.com",
            password = "password123"
        )

        // Assert
        assertEquals(
            "El rol por defecto debe ser USUARIO",
            "USUARIO",
            signupRequest.role
        )
        assertNull("Nombre debe ser null si no se proporciona", signupRequest.nombre)
        assertNull("Teléfono debe ser null si no se proporciona", signupRequest.telefono)
    }

    @Test
    fun `email válido debe pasar la validación`() {
        // Arrange
        val emailsValidos = listOf(
            "usuario@gmail.com",
            "test.user@duoc.cl",
            "maria_jose@fitlife.com",
            "admin123@company.com"
        )

        // Act & Assert
        emailsValidos.forEach { email ->
            assertTrue("El email $email debe ser válido", isValidEmail(email))
        }
    }

    @Test
    fun `email inválido debe fallar la validación`() {
        // Arrange
        val emailsInvalidos = listOf(
            "sin-arroba.com",
            "sin-dominio@",
            "@sin-usuario.com",
            "espacios en medio@test.com",
            "sin.extension@test"
        )

        // Act & Assert
        emailsInvalidos.forEach { email ->
            assertFalse("El email $email debe ser inválido", isValidEmail(email))
        }
    }

    @Test
    fun `password con longitud mínima debe ser válida`() {
        // Arrange
        val passwordMinima = "123456" // 6 caracteres

        // Act
        val resultado = isValidPassword(passwordMinima)

        // Assert
        assertTrue("Password de 6 caracteres debe ser válida", resultado)
    }

    @Test
    fun `password menor a longitud mínima debe ser inválida`() {
        // Arrange
        val passwordsInvalidas = listOf(
            "12345",    // 5 caracteres
            "abc",      // 3 caracteres
            "1",        // 1 carácter
            ""          // vacío
        )

        // Act & Assert
        passwordsInvalidas.forEach { password ->
            assertFalse(
                "Password '$password' debe ser inválida",
                isValidPassword(password)
            )
        }
    }

    @Test
    fun `password segura debe ser válida`() {
        // Arrange
        val passwordsSeguras = listOf(
            "Password123!",
            "MySecureP@ss2024",
            "ComplexPwd#456"
        )

        // Act & Assert
        passwordsSeguras.forEach { password ->
            assertTrue("Password segura '$password' debe ser válida", isValidPassword(password))
        }
    }

    @Test
    fun `teléfono con formato válido debe pasar validación`() {
        // Arrange
        val telefonosValidos = listOf(
            "+56912345678",
            "912345678",
            "+1234567890",
            "987654321"
        )

        // Act & Assert
        telefonosValidos.forEach { telefono ->
            assertTrue(
                "Teléfono $telefono debe ser válido",
                isValidPhone(telefono)
            )
        }
    }

    @Test
    fun `teléfono null debe ser válido (campo opcional)`() {
        // Arrange
        val telefonoNull: String? = null

        // Act
        val resultado = isValidPhone(telefonoNull)

        // Assert
        assertTrue("Teléfono null debe ser válido (campo opcional)", resultado)
    }

    @Test
    fun `signup request para entrenador debe tener rol correcto`() {
        // Arrange & Act
        val signupRequest = SignupRequest(
            email = "entrenador@fitlife.com",
            password = "trainerpass123",
            role = "ENTRENADOR",
            nombre = "Juan Pérez",
            telefono = "+56987654321"
        )

        // Assert
        assertEquals("El rol debe ser ENTRENADOR", "ENTRENADOR", signupRequest.role)
        assertNotNull("El nombre no debe ser null para entrenador", signupRequest.nombre)
    }

    @Test
    fun `signup request con campos opcionales null debe ser válido`() {
        // Arrange & Act
        val signupRequest = SignupRequest(
            email = "simple@test.com",
            password = "simplepass",
            role = "USUARIO",
            nombre = null,
            telefono = null
        )

        // Assert
        assertNotNull("Email no debe ser null", signupRequest.email)
        assertNotNull("Password no debe ser null", signupRequest.password)
        assertNull("Nombre puede ser null", signupRequest.nombre)
        assertNull("Teléfono puede ser null", signupRequest.telefono)
    }

    @Test
    fun `múltiples signup requests con diferentes roles deben ser válidos`() {
        // Arrange
        val usuarios = listOf(
            SignupRequest("user1@test.com", "pass123", "USUARIO", "User 1", null),
            SignupRequest("user2@test.com", "pass456", "ENTRENADOR", "Trainer 1", "+56911111111"),
            SignupRequest("user3@test.com", "pass789", "USUARIO", "User 3", "+56922222222")
        )

        // Act & Assert
        usuarios.forEachIndexed { index, usuario ->
            assertNotNull("Usuario $index debe tener email", usuario.email)
            assertNotNull("Usuario $index debe tener password", usuario.password)
            assertTrue(
                "Usuario $index debe tener un rol válido",
                usuario.role in listOf("USUARIO", "ENTRENADOR")
            )
        }
    }
}