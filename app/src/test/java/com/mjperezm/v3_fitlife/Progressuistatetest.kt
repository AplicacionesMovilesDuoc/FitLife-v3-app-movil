package com.mjperezm.v3_fitlife

import com.mjperezm.v3_fitlife.data.remote.ProgresoDto
import com.mjperezm.v3_fitlife.viewmodel.ProgressUiState
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para ProgressUiState
 * Valida el correcto funcionamiento del estado del ProgressViewModel
 */
class ProgressUiStateTest {

    @Test
    fun `estado inicial debe tener valores por defecto`() {
        // Arrange & Act
        val state = ProgressUiState()

        // Assert
        assertFalse("isLoading debe ser false por defecto", state.isLoading)
        assertTrue("progressList debe estar vacía por defecto", state.progressList.isEmpty())
        assertNull("error debe ser null por defecto", state.error)
        assertNull("successMessage debe ser null por defecto", state.successMessage)
    }

    @Test
    fun `estado con carga activa debe tener isLoading en true`() {
        // Arrange & Act
        val state = ProgressUiState(isLoading = true)

        // Assert
        assertTrue("isLoading debe estar en true durante la carga", state.isLoading)
        assertNull("error debe ser null durante la carga", state.error)
    }

    @Test
    fun `estado con lista de progreso debe contener los datos correctos`() {
        // Arrange
        val progresoList = listOf(
            ProgresoDto(
                _id = "1",
                usuario = "user123",
                fecha = "2024-12-15",
                peso = 75.5,
                medidas = "Pecho: 95cm",
                notas = "Buen progreso"
            ),
            ProgresoDto(
                _id = "2",
                usuario = "user123",
                fecha = "2024-12-10",
                peso = 76.0,
                medidas = "Pecho: 96cm",
                notas = "Primera medición"
            )
        )

        // Act
        val state = ProgressUiState(
            isLoading = false,
            progressList = progresoList
        )

        // Assert
        assertEquals("La lista debe tener 2 elementos", 2, state.progressList.size)
        assertEquals("El primer progreso debe tener peso 75.5", 75.5, state.progressList[0].peso, 0.01)
        assertEquals("El segundo progreso debe tener peso 76.0", 76.0, state.progressList[1].peso, 0.01)
        assertFalse("isLoading debe ser false con datos cargados", state.isLoading)
    }

    @Test
    fun `estado con error debe tener mensaje de error y no estar cargando`() {
        // Arrange
        val errorMessage = "Error al cargar progreso"

        // Act
        val state = ProgressUiState(
            isLoading = false,
            error = errorMessage
        )

        // Assert
        assertEquals("El mensaje de error debe ser el esperado", errorMessage, state.error)
        assertFalse("isLoading debe ser false cuando hay error", state.isLoading)
        assertTrue("progressList debe estar vacía cuando hay error", state.progressList.isEmpty())
    }

    @Test
    fun `estado con mensaje de éxito debe contener el mensaje correcto`() {
        // Arrange
        val successMessage = "Progreso registrado exitosamente"

        // Act
        val state = ProgressUiState(
            isLoading = false,
            successMessage = successMessage
        )

        // Assert
        assertEquals("El mensaje de éxito debe ser el esperado", successMessage, state.successMessage)
        assertFalse("isLoading debe ser false con mensaje de éxito", state.isLoading)
        assertNull("error debe ser null cuando hay éxito", state.error)
    }

    @Test
    fun `copiar estado debe mantener los valores no modificados`() {
        // Arrange
        val originalState = ProgressUiState(
            isLoading = false,
            progressList = listOf(
                ProgresoDto("1", "user123", "2024-12-15", 75.5, null, null)
            ),
            error = null,
            successMessage = "Éxito"
        )

        // Act
        val newState = originalState.copy(isLoading = true)

        // Assert
        assertTrue("El nuevo estado debe tener isLoading en true", newState.isLoading)
        assertEquals("progressList debe mantenerse igual", 1, newState.progressList.size)
        assertEquals("successMessage debe mantenerse igual", "Éxito", newState.successMessage)
    }

    @Test
    fun `estado limpio después de error debe eliminar mensaje de error`() {
        // Arrange
        val stateWithError = ProgressUiState(
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
    fun `estado con lista vacía debe ser válido`() {
        // Arrange & Act
        val state = ProgressUiState(
            isLoading = false,
            progressList = emptyList()
        )

        // Assert
        assertTrue("progressList debe estar vacía", state.progressList.isEmpty())
        assertEquals("El tamaño de la lista debe ser 0", 0, state.progressList.size)
        assertNull("error debe ser null", state.error)
    }
}