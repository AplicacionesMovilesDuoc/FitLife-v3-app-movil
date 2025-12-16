package com.mjperezm.v3_fitlife

import com.mjperezm.v3_fitlife.data.remote.dto.CitaDto
import com.mjperezm.v3_fitlife.viewmodel.AppointmentUiState
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para AppointmentUiState
 * Valida el correcto funcionamiento del estado del AppointmentViewModel
 */
class AppointmentUiStateTest {

    @Test
    fun `estado inicial debe tener valores por defecto`() {
        // Arrange & Act
        val state = AppointmentUiState()

        // Assert
        assertFalse("isLoading debe ser false por defecto", state.isLoading)
        assertTrue("appointments debe estar vacía por defecto", state.appointments.isEmpty())
        assertNull("error debe ser null por defecto", state.error)
        assertNull("successMessage debe ser null por defecto", state.successMessage)
    }

    @Test
    fun `estado con carga activa debe tener isLoading en true`() {
        // Arrange & Act
        val state = AppointmentUiState(isLoading = true)

        // Assert
        assertTrue("isLoading debe estar en true durante la carga", state.isLoading)
        assertNull("error debe ser null durante la carga", state.error)
        assertTrue("appointments debe estar vacía durante la carga", state.appointments.isEmpty())
    }

    @Test
    fun `estado con lista de citas debe contener los datos correctos`() {
        // Arrange
        val citas = listOf(
            CitaDto(
                _id = "cita1",
                usuario = "user123",
                entrenador = "trainer456",
                fecha = "2024-12-20",
                horaInicio = "10:00",
                horaFin = "11:00",
                estado = "CONFIRMADA",
                descripcion = "Sesión de entrenamiento"
            ),
            CitaDto(
                _id = "cita2",
                usuario = "user123",
                entrenador = "trainer456",
                fecha = "2024-12-22",
                horaInicio = "15:00",
                horaFin = "16:00",
                estado = "PENDIENTE",
                descripcion = "Evaluación física"
            )
        )

        // Act
        val state = AppointmentUiState(
            isLoading = false,
            appointments = citas
        )

        // Assert
        assertEquals("Debe haber 2 citas", 2, state.appointments.size)
        assertEquals("La primera cita debe tener estado CONFIRMADA", "CONFIRMADA", state.appointments[0].estado)
        assertEquals("La segunda cita debe tener estado PENDIENTE", "PENDIENTE", state.appointments[1].estado)
        assertFalse("isLoading debe ser false con datos cargados", state.isLoading)
    }

    @Test
    fun `estado con error debe tener mensaje de error y no estar cargando`() {
        // Arrange
        val errorMessage = "Error al cargar citas"

        // Act
        val state = AppointmentUiState(
            isLoading = false,
            error = errorMessage
        )

        // Assert
        assertEquals("El mensaje de error debe ser el esperado", errorMessage, state.error)
        assertFalse("isLoading debe ser false cuando hay error", state.isLoading)
        assertTrue("appointments debe estar vacía cuando hay error", state.appointments.isEmpty())
    }

    @Test
    fun `estado con mensaje de éxito debe contener el mensaje correcto`() {
        // Arrange
        val successMessage = "Cita agendada exitosamente"

        // Act
        val state = AppointmentUiState(
            isLoading = false,
            successMessage = successMessage
        )

        // Assert
        assertEquals("El mensaje de éxito debe ser el esperado", successMessage, state.successMessage)
        assertFalse("isLoading debe ser false con mensaje de éxito", state.isLoading)
        assertNull("error debe ser null cuando hay éxito", state.error)
    }

    @Test
    fun `estado con error de sesión expirada debe contener mensaje apropiado`() {
        // Arrange
        val errorMessage = "Sesión expirada. Por favor inicia sesión nuevamente"

        // Act
        val state = AppointmentUiState(
            isLoading = false,
            error = errorMessage
        )

        // Assert
        assertTrue(
            "El mensaje debe indicar sesión expirada",
            state.error?.contains("Sesión expirada") == true
        )
        assertFalse("isLoading debe ser false", state.isLoading)
    }

    @Test
    fun `estado con error de conexión debe contener mensaje apropiado`() {
        // Arrange
        val errorMessage = "Sin conexión a internet"

        // Act
        val state = AppointmentUiState(
            isLoading = false,
            error = errorMessage
        )

        // Assert
        assertTrue(
            "El mensaje debe indicar problema de conexión",
            state.error?.contains("conexión") == true
        )
    }

    @Test
    fun `copiar estado debe mantener valores no modificados`() {
        // Arrange
        val originalCitas = listOf(
            CitaDto("1", "user1", "trainer1", "2024-12-20", "10:00", "11:00", "CONFIRMADA", null)
        )

        val originalState = AppointmentUiState(
            isLoading = false,
            appointments = originalCitas,
            error = null,
            successMessage = "Éxito previo"
        )

        // Act
        val newState = originalState.copy(isLoading = true, successMessage = null)

        // Assert
        assertTrue("El nuevo estado debe tener isLoading en true", newState.isLoading)
        assertEquals("appointments debe mantenerse igual", 1, newState.appointments.size)
        assertNull("successMessage debe ser null en el nuevo estado", newState.successMessage)
        assertNull("error debe mantenerse null", newState.error)
    }

    @Test
    fun `estado limpio después de mensaje debe eliminar mensajes`() {
        // Arrange
        val stateWithMessages = AppointmentUiState(
            isLoading = false,
            error = "Error antiguo",
            successMessage = "Éxito antiguo"
        )

        // Act
        val cleanState = stateWithMessages.copy(
            error = null,
            successMessage = null
        )

        // Assert
        assertNull("error debe ser null después de limpiar", cleanState.error)
        assertNull("successMessage debe ser null después de limpiar", cleanState.successMessage)
    }

    @Test
    fun `citas ordenadas por fecha deben mantenerse en orden`() {
        // Arrange
        val citasOrdenadas = listOf(
            CitaDto("1", "user1", "trainer1", "2024-12-15", "09:00", "10:00", "CONFIRMADA", null),
            CitaDto("2", "user1", "trainer1", "2024-12-18", "14:00", "15:00", "PENDIENTE", null),
            CitaDto("3", "user1", "trainer1", "2024-12-22", "11:00", "12:00", "CONFIRMADA", null)
        )

        // Act
        val state = AppointmentUiState(
            isLoading = false,
            appointments = citasOrdenadas
        )

        // Assert
        assertEquals("Debe haber 3 citas", 3, state.appointments.size)
        assertEquals("Primera cita debe ser del 15", "2024-12-15", state.appointments[0].fecha)
        assertEquals("Segunda cita debe ser del 18", "2024-12-18", state.appointments[1].fecha)
        assertEquals("Tercera cita debe ser del 22", "2024-12-22", state.appointments[2].fecha)
    }

    @Test
    fun `estado con lista vacía de citas debe ser válido`() {
        // Arrange & Act
        val state = AppointmentUiState(
            isLoading = false,
            appointments = emptyList()
        )

        // Assert
        assertTrue("appointments debe estar vacía", state.appointments.isEmpty())
        assertEquals("El tamaño debe ser 0", 0, state.appointments.size)
        assertNull("error debe ser null", state.error)
        assertNull("successMessage debe ser null", state.successMessage)
    }

    @Test
    fun `estado con múltiples estados de cita debe ser válido`() {
        // Arrange
        val citas = listOf(
            CitaDto("1", "user1", "trainer1", "2024-12-20", "10:00", "11:00", "CONFIRMADA", "Sesión 1"),
            CitaDto("2", "user1", "trainer1", "2024-12-21", "11:00", "12:00", "PENDIENTE", "Sesión 2"),
            CitaDto("3", "user1", "trainer1", "2024-12-22", "12:00", "13:00", "CANCELADA", "Sesión 3")
        )

        // Act
        val state = AppointmentUiState(
            isLoading = false,
            appointments = citas
        )

        // Assert
        val estados = state.appointments.map { it.estado }
        assertTrue("Debe contener CONFIRMADA", estados.contains("CONFIRMADA"))
        assertTrue("Debe contener PENDIENTE", estados.contains("PENDIENTE"))
        assertTrue("Debe contener CANCELADA", estados.contains("CANCELADA"))
    }
}