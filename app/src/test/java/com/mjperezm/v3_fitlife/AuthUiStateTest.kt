package com.mjperezm.v3_fitlife

import com.mjperezm.v3_fitlife.viewmodel.AuthUiState
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para AuthUiState
 * Valida el correcto funcionamiento del estado del AuthViewModel
 */
class AuthUiStateTest {

    @Test
    fun `estado inicial debe tener valores por defecto`() {
        // Arrange & Act
        val state = AuthUiState()

        // Assert
        assertFalse("isLoading debe ser false por defecto", state.isLoading)
        assertFalse("isAuthenticated debe ser false por defecto", state.isAuthenticated)
        assertNull("error debe ser null por defecto", state.error)
        assertNull("successMessage debe ser null por defecto", state.successMessage)
    }

    @Test
    fun `estado autenticado debe tener isAuthenticated en true`() {
        // Arrange & Act
        val state = AuthUiState(isAuthenticated = true)

        // Assert
        assertTrue("isAuthenticated debe ser true", state.isAuthenticated)
        assertFalse("isLoading debe ser false", state.isLoading)
        assertNull("error debe ser null en estado exitoso", state.error)
    }

    @Test
    fun `estado con error de login debe contener mensaje apropiado`() {
        // Arrange
        val errorMessage = "Email o contraseña incorrectos"

        // Act
        val state = AuthUiState(
            isLoading = false,
            isAuthenticated = false,
            error = errorMessage
        )

        // Assert
        assertEquals("El mensaje de error debe ser el esperado", errorMessage, state.error)
        assertFalse("isAuthenticated debe ser false con error", state.isAuthenticated)
    }

    @Test
    fun `estado cargando debe tener isLoading en true`() {
        // Arrange & Act
        val state = AuthUiState(isLoading = true)

        // Assert
        assertTrue("isLoading debe ser true durante la autenticación", state.isLoading)
        assertFalse("isAuthenticated debe ser false durante carga", state.isAuthenticated)
        assertNull("error debe ser null durante carga", state.error)
    }

    @Test
    fun `estado con mensaje de éxito debe contener el mensaje correcto`() {
        // Arrange
        val successMessage = "¡Bienvenido a FitLife!"

        // Act
        val state = AuthUiState(
            isLoading = false,
            isAuthenticated = true,
            successMessage = successMessage
        )

        // Assert
        assertEquals("El mensaje de éxito debe ser el esperado", successMessage, state.successMessage)
        assertTrue("isAuthenticated debe ser true con éxito", state.isAuthenticated)
        assertNull("error debe ser null con éxito", state.error)
    }

    @Test
    fun `copiar estado debe mantener valores no modificados`() {
        // Arrange
        val originalState = AuthUiState(
            isLoading = false,
            isAuthenticated = false,
            error = "Error antiguo"
        )

        // Act
        val newState = originalState.copy(isLoading = true, error = null)

        // Assert
        assertTrue("El nuevo estado debe tener isLoading en true", newState.isLoading)
        assertFalse("isAuthenticated debe mantenerse igual", newState.isAuthenticated)
        assertNull("error debe ser null en el nuevo estado", newState.error)
    }

    @Test
    fun `logout debe resetear estado a no autenticado`() {
        // Arrange
        val authenticatedState = AuthUiState(
            isAuthenticated = true,
            successMessage = "Login exitoso"
        )

        // Act
        val loggedOutState = AuthUiState(isAuthenticated = false)

        // Assert
        assertFalse("isAuthenticated debe ser false después de logout", loggedOutState.isAuthenticated)
        assertNull("successMessage debe ser null después de logout", loggedOutState.successMessage)
    }
}