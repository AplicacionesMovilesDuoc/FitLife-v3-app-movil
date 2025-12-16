package com.mjperezm.v3_fitlife

import android.net.Uri
import com.mjperezm.v3_fitlife.data.remote.dto.UserDto
import com.mjperezm.v3_fitlife.viewmodel.ProfileUiState
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Pruebas unitarias para ProfileUiState
 * Valida el correcto funcionamiento del estado del ProfileViewModel
 */
class ProfileUiStateTest {

    @Test
    fun `estado inicial debe tener valores por defecto`() {
        // Arrange & Act
        val state = ProfileUiState()

        // Assert
        assertFalse("isLoading debe ser false por defecto", state.isLoading)
        assertNull("user debe ser null por defecto", state.user)
        assertNull("avatarUri debe ser null por defecto", state.avatarUri)
        assertNull("error debe ser null por defecto", state.error)
    }

    @Test
    fun `estado con carga activa debe tener isLoading en true`() {
        // Arrange & Act
        val state = ProfileUiState(isLoading = true)

        // Assert
        assertTrue("isLoading debe estar en true durante la carga", state.isLoading)
        assertNull("user debe ser null durante la carga", state.user)
        assertNull("error debe ser null durante la carga", state.error)
    }

    @Test
    fun `estado con usuario cargado debe contener los datos del usuario`() {
        // Arrange
        val userData = UserDto(
            _id = "user123",
            email = "maria@test.com",
            role = "USUARIO",
            nombre = "María José",
            telefono = "+56912345678"
        )

        // Act
        val state = ProfileUiState(
            isLoading = false,
            user = userData
        )

        // Assert
        assertFalse("isLoading debe ser false con datos cargados", state.isLoading)
        assertNotNull("user no debe ser null", state.user)
        assertEquals("El email debe ser correcto", "maria@test.com", state.user?.email)
        assertEquals("El nombre debe ser correcto", "María José", state.user?.nombre)
        assertEquals("El rol debe ser USUARIO", "USUARIO", state.user?.role)
        assertNull("error debe ser null con datos exitosos", state.error)
    }

    @Test
    fun `estado con avatar debe contener la URI del avatar`() {
        // Arrange
        val mockUri = mock(Uri::class.java)

        // Act
        val state = ProfileUiState(
            isLoading = false,
            avatarUri = mockUri
        )

        // Assert
        assertNotNull("avatarUri no debe ser null", state.avatarUri)
        assertEquals("La URI debe ser la esperada", mockUri, state.avatarUri)
    }

    @Test
    fun `estado con error debe tener mensaje de error y no estar cargando`() {
        // Arrange
        val errorMessage = "Error al cargar perfil"

        // Act
        val state = ProfileUiState(
            isLoading = false,
            error = errorMessage
        )

        // Assert
        assertEquals("El mensaje de error debe ser el esperado", errorMessage, state.error)
        assertFalse("isLoading debe ser false cuando hay error", state.isLoading)
        assertNull("user debe ser null cuando hay error", state.user)
    }

    @Test
    fun `estado con error de sesión expirada debe contener mensaje apropiado`() {
        // Arrange
        val sessionErrorMessage = "Sesión expirada. Por favor inicia sesión nuevamente"

        // Act
        val state = ProfileUiState(
            isLoading = false,
            error = sessionErrorMessage
        )

        // Assert
        assertEquals(
            "El mensaje debe indicar sesión expirada",
            sessionErrorMessage,
            state.error
        )
        assertNull("user debe ser null con error de sesión", state.user)
    }

    @Test
    fun `estado con error de conexión debe contener mensaje apropiado`() {
        // Arrange
        val connectionErrorMessage = "Sin conexión a internet. Verifica tu red."

        // Act
        val state = ProfileUiState(
            isLoading = false,
            error = connectionErrorMessage
        )

        // Assert
        assertTrue(
            "El mensaje debe indicar problema de conexión",
            state.error?.contains("conexión") == true
        )
        assertFalse("isLoading debe ser false", state.isLoading)
    }

    @Test
    fun `copiar estado debe mantener valores no modificados`() {
        // Arrange
        val originalUser = UserDto(
            _id = "user123",
            email = "test@test.com",
            role = "USUARIO",
            nombre = "Test User",
            telefono = null
        )

        val originalState = ProfileUiState(
            isLoading = false,
            user = originalUser,
            avatarUri = null,
            error = null
        )

        // Act
        val newState = originalState.copy(isLoading = true)

        // Assert
        assertTrue("El nuevo estado debe tener isLoading en true", newState.isLoading)
        assertEquals("El usuario debe mantenerse igual", originalUser, newState.user)
        assertNull("avatarUri debe mantenerse null", newState.avatarUri)
        assertNull("error debe mantenerse null", newState.error)
    }

    @Test
    fun `estado limpio después de error debe eliminar mensaje de error`() {
        // Arrange
        val stateWithError = ProfileUiState(
            isLoading = false,
            error = "Hubo un error"
        )

        // Act
        val cleanState = stateWithError.copy(error = null)

        // Assert
        assertNull("error debe ser null después de limpiar", cleanState.error)
        assertFalse("isLoading debe mantenerse en false", cleanState.isLoading)
    }

    @Test
    fun `estado completo con todos los campos debe ser válido`() {
        // Arrange
        val mockUri = mock(Uri::class.java)
        val userData = UserDto(
            _id = "user123",
            email = "complete@test.com",
            role = "ENTRENADOR",
            nombre = "Usuario Completo",
            telefono = "+56987654321"
        )

        // Act
        val state = ProfileUiState(
            isLoading = false,
            user = userData,
            avatarUri = mockUri,
            error = null
        )

        // Assert
        assertFalse("isLoading debe ser false", state.isLoading)
        assertNotNull("user no debe ser null", state.user)
        assertNotNull("avatarUri no debe ser null", state.avatarUri)
        assertNull("error debe ser null", state.error)
        assertEquals("El rol debe ser ENTRENADOR", "ENTRENADOR", state.user?.role)
    }
}