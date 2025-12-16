package com.mjperezm.v3_fitlife

import com.mjperezm.v3_fitlife.data.remote.dto.CitaDto
import com.mjperezm.v3_fitlife.data.remote.dto.CreateCitaDto
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pruebas unitarias para validación de DTOs de Cita
 */
class CitaDtoValidationTest {

    @Test
    fun `cita dto con datos completos debe crearse correctamente`() {
        // Arrange & Act
        val cita = CitaDto(
            id = "cita123",
            usuario = "user456",
            usuarioNombre = "María José",
            entrenador = "trainer789",
            entrenadorNombre = "Juan Pérez",
            fecha = "2024-12-20",
            horaInicio = "10:00",
            horaFin = "11:00",
            sucursal = "Sucursal Principal",
            estado = "CONFIRMADA",
            descripcion = "Sesión de entrenamiento",
            createdAt = "2024-12-15T10:00:00"
        )

        // Assert
        assertEquals("ID debe ser correcto", "cita123", cita.id)
        assertEquals("Estado debe ser CONFIRMADA", "CONFIRMADA", cita.estado)
        assertNotNull("Descripción no debe ser null", cita.descripcion)
    }

    @Test
    fun `create cita dto debe aceptar descripción opcional`() {
        // Arrange & Act
        val cita = CreateCitaDto(
            entrenador = "trainer123",
            fecha = "2024-12-20",
            horaInicio = "10:00",
            horaFin = "11:00",
            descripcion = null
        )

        // Assert
        assertNotNull("Entrenador no debe ser null", cita.entrenador)
        assertNotNull("Fecha no debe ser null", cita.fecha)
        assertNull("Descripción puede ser null", cita.descripcion)
    }

    @Test
    fun `fecha de cita debe tener formato válido`() {
        // Arrange
        val fecha = "2024-12-20"
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
        assertTrue("La fecha debe tener formato yyyy-MM-dd válido", isValid)
    }



    @Test
    fun `estado de cita debe ser uno de los valores permitidos`() {
        // Arrange
        val estadosValidos = listOf("CONFIRMADA", "PENDIENTE", "CANCELADA", "COMPLETADA")
        val cita = CitaDto(
            id = "1",
            usuario = "user1",
            usuarioNombre = null,
            entrenador = "trainer1",
            entrenadorNombre = null,
            fecha = "2024-12-20",
            horaInicio = "10:00",
            horaFin = "11:00",
            estado = "CONFIRMADA"
        )

        // Assert
        assertTrue("El estado debe ser uno de los válidos",
            estadosValidos.contains(cita.estado))
    }

    @Test
    fun `hora fin debe ser posterior a hora inicio`() {
        // Arrange
        val horaInicio = "10:00"
        val horaFin = "11:00"
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Act
        val inicio = format.parse(horaInicio)!!
        val fin = format.parse(horaFin)!!

        // Assert
        assertTrue("Hora fin debe ser posterior a hora inicio", fin.after(inicio))
    }

    @Test
    fun `múltiples citas pueden tener diferentes estados`() {
        // Arrange
        val citas = listOf(
            CitaDto("1", "u1", null, "t1", null, "2024-12-20", "10:00", "11:00", estado = "CONFIRMADA"),
            CitaDto("2", "u1", null, "t1", null, "2024-12-21", "11:00", "12:00", estado = "PENDIENTE"),
            CitaDto("3", "u1", null, "t1", null, "2024-12-22", "12:00", "13:00", estado = "CANCELADA")
        )

        // Act
        val estados = citas.map { it.estado }.toSet()

        // Assert
        assertEquals("Debe haber 3 estados diferentes", 3, estados.size)
        assertTrue("Debe incluir CONFIRMADA", estados.contains("CONFIRMADA"))
        assertTrue("Debe incluir PENDIENTE", estados.contains("PENDIENTE"))
        assertTrue("Debe incluir CANCELADA", estados.contains("CANCELADA"))
    }
}