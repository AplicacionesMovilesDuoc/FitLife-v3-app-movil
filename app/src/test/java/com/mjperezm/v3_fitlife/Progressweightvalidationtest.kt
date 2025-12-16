package com.mjperezm.v3_fitlife

import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para la validación de peso en el registro de progreso
 * Simula la lógica de validación que usa ProgressViewModel.addProgress()
 */
class ProgressWeightValidationTest {

    /**
     * Función helper que simula la validación de peso del ViewModel
     */
    private fun validateWeight(peso: Double): String? {
        return if (peso <= 0) {
            "El peso debe ser mayor a 0"
        } else {
            null
        }
    }

    @Test
    fun `peso válido positivo debe pasar la validación`() {
        // Arrange
        val pesoValido = 70.5

        // Act
        val resultado = validateWeight(pesoValido)

        // Assert
        assertNull("Un peso válido positivo no debe generar error", resultado)
    }

    @Test
    fun `peso cero debe fallar la validación`() {
        // Arrange
        val pesoCero = 0.0

        // Act
        val resultado = validateWeight(pesoCero)

        // Assert
        assertNotNull("Peso cero debe generar un error", resultado)
        assertEquals(
            "El mensaje de error debe ser el esperado",
            "El peso debe ser mayor a 0",
            resultado
        )
    }

    @Test
    fun `peso negativo debe fallar la validación`() {
        // Arrange
        val pesoNegativo = -5.0

        // Act
        val resultado = validateWeight(pesoNegativo)

        // Assert
        assertNotNull("Peso negativo debe generar un error", resultado)
        assertEquals(
            "El mensaje de error debe indicar que el peso debe ser mayor a 0",
            "El peso debe ser mayor a 0",
            resultado
        )
    }

    @Test
    fun `peso decimal válido debe pasar la validación`() {
        // Arrange
        val pesoDecimal = 68.75

        // Act
        val resultado = validateWeight(pesoDecimal)

        // Assert
        assertNull("Un peso decimal válido debe pasar la validación", resultado)
    }

    @Test
    fun `peso muy pequeño positivo debe pasar la validación`() {
        // Arrange
        val pesoMuyPequeño = 0.1

        // Act
        val resultado = validateWeight(pesoMuyPequeño)

        // Assert
        assertNull("Un peso muy pequeño pero positivo debe pasar la validación", resultado)
    }

    @Test
    fun `peso muy grande debe pasar la validación`() {
        // Arrange
        val pesoMuyGrande = 200.0

        // Act
        val resultado = validateWeight(pesoMuyGrande)

        // Assert
        assertNull("Un peso muy grande pero válido debe pasar la validación", resultado)
    }

    @Test
    fun `peso en el límite (casi cero) debe fallar la validación`() {
        // Arrange
        val pesoLimite = 0.0

        // Act
        val resultado = validateWeight(pesoLimite)

        // Assert
        assertNotNull("Peso en el límite de cero debe fallar", resultado)
    }

    @Test
    fun `múltiples validaciones de pesos válidos`() {
        // Arrange
        val pesosValidos = listOf(50.0, 65.5, 80.0, 95.3, 120.0)

        // Act & Assert
        pesosValidos.forEach { peso ->
            val resultado = validateWeight(peso)
            assertNull("El peso $peso debe ser válido", resultado)
        }
    }

    @Test
    fun `múltiples validaciones de pesos inválidos`() {
        // Arrange
        val pesosInvalidos = listOf(-10.0, -1.5, 0.0, -50.0)

        // Act & Assert
        pesosInvalidos.forEach { peso ->
            val resultado = validateWeight(peso)
            assertNotNull("El peso $peso debe ser inválido", resultado)
            assertEquals(
                "El mensaje debe ser consistente para peso $peso",
                "El peso debe ser mayor a 0",
                resultado
            )
        }
    }
}