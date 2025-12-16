package com.mjperezm.v3_fitlife

import com.mjperezm.v3_fitlife.data.remote.CreatedByDto
import com.mjperezm.v3_fitlife.data.remote.PlanDto
import com.mjperezm.v3_fitlife.viewmodel.PlanUiState
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para PlanUiState
 * Valida el correcto funcionamiento del estado del PlanViewModel
 */
class PlanUiStateTest {

    @Test
    fun `estado inicial debe tener valores por defecto`() {
        // Arrange & Act
        val state = PlanUiState()

        // Assert
        assertFalse("isLoading debe ser false por defecto", state.isLoading)
        assertTrue("planesEntrenamiento debe estar vacía", state.planesEntrenamiento.isEmpty())
        assertTrue("planesNutricionales debe estar vacía", state.planesNutricionales.isEmpty())
        assertNull("planSeleccionado debe ser null", state.planSeleccionado)
        assertNull("error debe ser null", state.error)
    }

    @Test
    fun `estado con planes cargados debe contener la lista correcta`() {
        // Arrange
        val planes = listOf(
            PlanDto(
                _id = "plan1",
                nombre = "Cardio Intenso",
                descripcion = "Plan de cardio",
                duracion = "45 min",
                dificultad = "intermedio",
                tipo = "cardio",
                ejercicios = null,
                disponible = true,
                imagen = null,
                imagenThumbnail = null,
                createdBy = null
            ),
            PlanDto(
                _id = "plan2",
                nombre = "Fuerza Total",
                descripcion = "Plan de fuerza",
                duracion = "60 min",
                dificultad = "avanzado",
                tipo = "fuerza",
                ejercicios = null,
                disponible = true,
                imagen = null,
                imagenThumbnail = null,
                createdBy = null
            )
        )

        // Act
        val state = PlanUiState(
            isLoading = false,
            planesEntrenamiento = planes
        )

        // Assert
        assertEquals("Debe haber 2 planes", 2, state.planesEntrenamiento.size)
        assertEquals("Primer plan debe ser Cardio Intenso", "Cardio Intenso", state.planesEntrenamiento[0].nombre)
        assertFalse("isLoading debe ser false", state.isLoading)
    }

    @Test
    fun `estado con plan seleccionado debe contener el plan correcto`() {
        // Arrange
        val plan = PlanDto(
            _id = "plan123",
            nombre = "Plan Test",
            descripcion = "Descripción test",
            duracion = "30 min",
            dificultad = "principiante",
            tipo = "yoga",
            ejercicios = null,
            disponible = true,
            imagen = null,
            imagenThumbnail = null,
            createdBy = CreatedByDto(_id = "creator123", email = "trainer@test.com")
        )

        // Act
        val state = PlanUiState(
            isLoading = false,
            planSeleccionado = plan
        )

        // Assert
        assertNotNull("planSeleccionado no debe ser null", state.planSeleccionado)
        assertEquals("El plan debe tener el ID correcto", "plan123", state.planSeleccionado?._id)
        assertEquals("El plan debe tener el nombre correcto", "Plan Test", state.planSeleccionado?.nombre)
    }

    @Test
    fun `estado con error debe contener mensaje de error`() {
        // Arrange
        val errorMessage = "Error al cargar planes"

        // Act
        val state = PlanUiState(
            isLoading = false,
            error = errorMessage
        )

        // Assert
        assertEquals("El mensaje de error debe ser el esperado", errorMessage, state.error)
        assertTrue("planesEntrenamiento debe estar vacía con error", state.planesEntrenamiento.isEmpty())
    }

    @Test
    fun `estado cargando debe tener isLoading en true`() {
        // Arrange & Act
        val state = PlanUiState(isLoading = true)

        // Assert
        assertTrue("isLoading debe ser true durante la carga", state.isLoading)
        assertNull("error debe ser null durante carga", state.error)
    }

    @Test
    fun `copiar estado debe mantener valores no modificados`() {
        // Arrange
        val plan = PlanDto("1", "Test", "Desc", null, null, null, null, true, null, null, null)
        val originalState = PlanUiState(
            isLoading = false,
            planSeleccionado = plan
        )

        // Act
        val newState = originalState.copy(isLoading = true)

        // Assert
        assertTrue("El nuevo estado debe tener isLoading en true", newState.isLoading)
        assertEquals("planSeleccionado debe mantenerse", plan, newState.planSeleccionado)
    }

    @Test
    fun `estado con planes filtrados por dificultad debe ser válido`() {
        // Arrange
        val planes = listOf(
            PlanDto("1", "Plan1", "Desc1", null, "principiante", null, null, true, null, null, null),
            PlanDto("2", "Plan2", "Desc2", null, "principiante", null, null, true, null, null, null)
        )

        // Act
        val state = PlanUiState(planesEntrenamiento = planes)

        // Assert
        assertTrue("Todos los planes deben tener la misma dificultad",
            state.planesEntrenamiento.all { it.dificultad == "principiante" })
    }
}