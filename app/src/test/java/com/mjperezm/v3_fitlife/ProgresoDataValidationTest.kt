package com.mjperezm.v3_fitlife

import com.mjperezm.v3_fitlife.data.remote.CreateProgresoDto
import com.mjperezm.v3_fitlife.data.remote.ProgresoDto
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pruebas unitarias para validación de datos de Progreso
 */
class ProgresoDataValidationTest {

    @Test
    fun `progreso dto con datos válidos debe crearse correctamente`() {
        // Arrange & Act
        val progreso = ProgresoDto(
            _id = "prog123",
            userId = "user456",
            fecha = "2024-12-15",
            peso = 75.5,
            medidas = "Pecho: 95cm",
            notas = "Buen progreso"
        )

        // Assert
        assertEquals("ID debe ser correcto", "prog123", progreso._id)
        assertEquals("Peso debe ser 75.5", 75.5, progreso.peso, 0.01)
        assertNotNull("Medidas no debe ser null", progreso.medidas)
        assertNotNull("Notas no debe ser null", progreso.notas)
    }

    @Test
    fun `create progreso dto debe aceptar campos opcionales nulos`() {
        // Arrange & Act
        val progreso = CreateProgresoDto(
            fecha = "2024-12-15",
            peso = 70.0,
            medidas = null,
            notas = null
        )

        // Assert
        assertNotNull("Fecha no debe ser null", progreso.fecha)
        assertEquals("Peso debe ser correcto", 70.0, progreso.peso, 0.01)
        assertNull("Medidas puede ser null", progreso.medidas)
        assertNull("Notas puede ser null", progreso.notas)
    }

    @Test
    fun `fecha debe tener formato válido yyyy-MM-dd`() {
        // Arrange
        val fecha = "2024-12-15"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false

        // Act
        var isValid = true
        try {
            dateFormat.parse(fecha)
        } catch (e: Exception) {
            isValid = false
        }

        // Assert
        assertTrue("La fecha debe tener formato válido", isValid)
    }

    @Test
    fun `peso debe ser un número positivo`() {
        // Arrange & Act
        val progreso = CreateProgresoDto(
            fecha = "2024-12-15",
            peso = 68.5,
            medidas = null,
            notas = null
        )

        // Assert
        assertTrue("El peso debe ser mayor a 0", progreso.peso > 0)
    }

    @Test
    fun `peso debe aceptar decimales`() {
        // Arrange
        val pesos = listOf(70.5, 68.25, 75.75, 82.1)

        // Act & Assert
        pesos.forEach { peso ->
            val progreso = CreateProgresoDto("2024-12-15", peso, null, null)
            assertEquals("El peso decimal debe preservarse", peso, progreso.peso, 0.001)
        }
    }

    @Test
    fun `medidas debe aceptar strings largos`() {
        // Arrange
        val medidasLargas = "Pecho: 95cm, Cintura: 80cm, Brazo: 35cm, Pierna: 55cm"

        // Act
        val progreso = CreateProgresoDto(
            fecha = "2024-12-15",
            peso = 75.0,
            medidas = medidasLargas,
            notas = null
        )

        // Assert
        assertEquals("Medidas debe preservar el string completo", medidasLargas, progreso.medidas)
        assertTrue("Medidas debe tener más de 20 caracteres", progreso.medidas!!.length > 20)
    }

    @Test
    fun `notas debe aceptar strings con saltos de línea`() {
        // Arrange
        val notasMultilinea = "Día 1: Buen entrenamiento\nDía 2: Mejoró resistencia\nDía 3: Excelente"

        // Act
        val progreso = CreateProgresoDto(
            fecha = "2024-12-15",
            peso = 75.0,
            medidas = null,
            notas = notasMultilinea
        )

        // Assert
        assertNotNull("Notas no debe ser null", progreso.notas)
        assertTrue("Notas debe contener saltos de línea", progreso.notas!!.contains("\n"))
    }
}