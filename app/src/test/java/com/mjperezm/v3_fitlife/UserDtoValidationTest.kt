package com.mjperezm.v3_fitlife

import com.mjperezm.v3_fitlife.data.remote.dto.UserDto
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para UserDto
 * Valida la estructura y datos del usuario
 */
class UserDtoValidationTest {

    @Test
    fun `user dto con datos completos debe crearse correctamente`() {
        // Arrange & Act
        val user = UserDto(
            id = "user123",
            email = "maria@test.com",
            role = "USUARIO",
            isActive = true,
            emailVerified = false,
            telefono = "+56912345678",
            edad = 25,
            peso = 65.5,
            altura = 165.0,
            objetivo = "Perder peso"
        )

        // Assert
        assertEquals("ID debe ser correcto", "user123", user.id)
        assertEquals("Email debe ser correcto", "maria@test.com", user.email)
        assertEquals("Role debe ser USUARIO", "USUARIO", user.role)
        assertTrue("isActive debe ser true", user.isActive == true)
    }

    @Test
    fun `user dto con campos opcionales nulos debe ser válido`() {
        // Arrange & Act
        val user = UserDto(
            id = "user456",
            email = "test@test.com",
            role = "USUARIO",
            isActive = true,
            emailVerified = false,
            telefono = null,
            edad = null,
            peso = null,
            altura = null,
            objetivo = null
        )

        // Assert
        assertNotNull("ID no debe ser null", user.id)
        assertNotNull("Email no debe ser null", user.email)
        assertNull("Peso puede ser null", user.peso)
    }

    @Test
    fun `role debe ser uno de los valores permitidos`() {
        // Arrange
        val rolesValidos = listOf("USUARIO", "ENTRENADOR", "ADMIN")
        val user = UserDto(
            id = "user1",
            email = "test@test.com",
            role = "USUARIO"
        )

        // Assert
        assertTrue("El role debe ser uno de los válidos",
            rolesValidos.contains(user.role))
    }

    @Test
    fun `peso debe aceptar valores decimales`() {
        // Arrange & Act
        val user = UserDto(
            id = "user1",
            email = "test@test.com",
            role = "USUARIO",
            peso = 68.75
        )

        // Assert
        assertNotNull("Peso no debe ser null", user.peso)
        assertEquals("Peso debe ser 68.75", 68.75, user.peso!!, 0.01)
        assertTrue("Peso debe ser mayor a 0", user.peso!! > 0)
    }

    @Test
    fun `altura debe estar en rango válido`() {
        // Arrange & Act
        val user = UserDto(
            id = "user1",
            email = "test@test.com",
            role = "USUARIO",
            altura = 165.0
        )

        // Assert
        assertNotNull("Altura no debe ser null", user.altura)
        assertTrue("Altura debe ser mayor a 0", user.altura!! > 0)
        assertTrue("Altura debe estar en rango razonable", user.altura!! in 100.0..250.0)
    }

    @Test
    fun `edad debe ser un número positivo`() {
        // Arrange & Act
        val user = UserDto(
            id = "user1",
            email = "test@test.com",
            role = "USUARIO",
            edad = 25
        )

        // Assert
        assertNotNull("Edad no debe ser null", user.edad)
        assertTrue("Edad debe ser mayor a 0", user.edad!! > 0)
        assertTrue("Edad debe estar en rango razonable", user.edad!! in 1..120)
    }

    @Test
    fun `múltiples usuarios con diferentes roles deben ser válidos`() {
        // Arrange
        val usuarios = listOf(
            UserDto("1", "user1@test.com", "USUARIO"),
            UserDto("2", "trainer@test.com", "ENTRENADOR"),
            UserDto("3", "admin@test.com", "ADMIN")
        )

        // Act
        val roles = usuarios.map { it.role }.toSet()

        // Assert
        assertEquals("Debe haber 3 roles diferentes", 3, roles.size)
        assertTrue("Debe incluir USUARIO", roles.contains("USUARIO"))
        assertTrue("Debe incluir ENTRENADOR", roles.contains("ENTRENADOR"))
        assertTrue("Debe incluir ADMIN", roles.contains("ADMIN"))
    }
}