package com.mjperezm.v3_fitlife

import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pruebas unitarias para las utilidades de formateo de fechas
 * Simula la lógica de parseo de fechas que usa ProgressViewModel
 */
class DateFormattingUtilTest {

    /**
     * Función helper que simula el parseo de fechas del ViewModel
     * Intenta parsear con formato ISO primero, luego con formato simple
     */
    private fun parseDateToTimestamp(fecha: String): Long {
        return try {
            // Intentar parsear la fecha en formato ISO
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                .parse(fecha)?.time ?: 0L
        } catch (e: Exception) {
            // Si falla, intentar con formato simple
            try {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .parse(fecha)?.time ?: 0L
            } catch (e: Exception) {
                0L
            }
        }
    }

    /**
     * Función helper para formatear fecha actual
     */
    private fun formatCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    @Test
    fun `fecha en formato ISO debe parsearse correctamente`() {
        // Arrange
        val fechaISO = "2024-12-15T14:30:00"

        // Act
        val timestamp = parseDateToTimestamp(fechaISO)

        // Assert
        assertTrue("El timestamp debe ser mayor a 0 para fecha válida", timestamp > 0)
        assertNotEquals("El timestamp no debe ser 0", 0L, timestamp)
    }

    @Test
    fun `fecha en formato simple debe parsearse correctamente`() {
        // Arrange
        val fechaSimple = "2024-12-15"

        // Act
        val timestamp = parseDateToTimestamp(fechaSimple)

        // Assert
        assertTrue("El timestamp debe ser mayor a 0 para fecha válida", timestamp > 0)
        assertNotEquals("El timestamp no debe ser 0", 0L, timestamp)
    }

    @Test
    fun `fecha inválida debe retornar timestamp 0`() {
        // Arrange
        val fechaInvalida = "fecha-invalida"

        // Act
        val timestamp = parseDateToTimestamp(fechaInvalida)

        // Assert
        assertEquals("Fecha inválida debe retornar 0", 0L, timestamp)
    }

    @Test
    fun `fecha vacía debe retornar timestamp 0`() {
        // Arrange
        val fechaVacia = ""

        // Act
        val timestamp = parseDateToTimestamp(fechaVacia)

        // Assert
        assertEquals("Fecha vacía debe retornar 0", 0L, timestamp)
    }

    @Test
    fun `fechas más recientes deben tener timestamps mayores`() {
        // Arrange
        val fechaAntigua = "2024-01-01"
        val fechaReciente = "2024-12-15"

        // Act
        val timestampAntiguo = parseDateToTimestamp(fechaAntigua)
        val timestampReciente = parseDateToTimestamp(fechaReciente)

        // Assert
        assertTrue(
            "La fecha más reciente debe tener un timestamp mayor",
            timestampReciente > timestampAntiguo
        )
    }

    @Test
    fun `formato de fecha actual debe ser yyyy-MM-dd`() {
        // Arrange & Act
        val fechaFormateada = formatCurrentDate()

        // Assert
        assertTrue("La fecha debe tener formato yyyy-MM-dd",
            fechaFormateada.matches("\\d{4}-\\d{2}-\\d{2}".toRegex()))
        assertEquals("La fecha debe tener 10 caracteres", 10, fechaFormateada.length)
        assertTrue("La fecha debe contener guiones", fechaFormateada.contains("-"))
    }

    @Test
    fun `múltiples fechas ISO deben parsearse correctamente`() {
        // Arrange
        val fechasISO = listOf(
            "2024-01-15T10:30:00",
            "2024-06-20T14:45:30",
            "2024-12-31T23:59:59"
        )

        // Act & Assert
        fechasISO.forEach { fecha ->
            val timestamp = parseDateToTimestamp(fecha)
            assertTrue(
                "La fecha $fecha debe parsearse correctamente",
                timestamp > 0
            )
        }
    }

    @Test
    fun `múltiples fechas simples deben parsearse correctamente`() {
        // Arrange
        val fechasSimples = listOf(
            "2024-01-01",
            "2024-06-15",
            "2024-12-31"
        )

        // Act & Assert
        fechasSimples.forEach { fecha ->
            val timestamp = parseDateToTimestamp(fecha)
            assertTrue(
                "La fecha $fecha debe parsearse correctamente",
                timestamp > 0
            )
        }
    }

    @Test
    fun `ordenamiento por timestamp debe funcionar correctamente`() {
        // Arrange
        val fechas = listOf(
            "2024-03-15T10:00:00",
            "2024-01-10T08:30:00",
            "2024-12-20T16:45:00",
            "2024-06-05T12:00:00"
        )

        // Act
        val timestamps = fechas.map { parseDateToTimestamp(it) }
        val sortedDesc = timestamps.sortedDescending()

        // Assert
        assertEquals(
            "El primer elemento debe ser el timestamp más reciente",
            sortedDesc[0],
            parseDateToTimestamp("2024-12-20T16:45:00")
        )
        assertEquals(
            "El último elemento debe ser el timestamp más antiguo",
            sortedDesc.last(),
            parseDateToTimestamp("2024-01-10T08:30:00")
        )
    }

    @Test
    fun `fechas con mismo día pero diferente hora deben tener timestamps diferentes`() {
        // Arrange
        val fechaMañana = "2024-12-15T09:00:00"
        val fechaTarde = "2024-12-15T18:00:00"

        // Act
        val timestampMañana = parseDateToTimestamp(fechaMañana)
        val timestampTarde = parseDateToTimestamp(fechaTarde)

        // Assert
        assertNotEquals(
            "Fechas con diferente hora deben tener timestamps diferentes",
            timestampMañana,
            timestampTarde
        )
        assertTrue(
            "La hora de la tarde debe tener timestamp mayor",
            timestampTarde > timestampMañana
        )
    }

    @Test
    fun `comparación de fechas simples debe mantener orden cronológico`() {
        // Arrange
        val fecha1 = "2024-01-01"
        val fecha2 = "2024-06-15"
        val fecha3 = "2024-12-31"

        // Act
        val timestamps = listOf(
            parseDateToTimestamp(fecha1),
            parseDateToTimestamp(fecha2),
            parseDateToTimestamp(fecha3)
        )

        // Assert
        assertTrue("Las fechas deben estar en orden cronológico",
            timestamps[0] < timestamps[1] && timestamps[1] < timestamps[2])
    }

    @Test
    fun `fallback a formato simple debe funcionar cuando ISO falla`() {
        // Arrange
        val fechaSoloSimple = "2024-12-15" // No tiene hora

        // Act
        val timestamp = parseDateToTimestamp(fechaSoloSimple)

        // Assert
        assertTrue("El timestamp debe ser válido incluso sin hora", timestamp > 0)

        // Verificar que corresponde al mismo día
        val fechaParseada = Date(timestamp)
        val calendar = Calendar.getInstance()
        calendar.time = fechaParseada

        assertEquals("El año debe ser 2024", 2024, calendar.get(Calendar.YEAR))
        assertEquals("El mes debe ser diciembre (11 en Calendar)", 11, calendar.get(Calendar.MONTH))
        assertEquals("El día debe ser 15", 15, calendar.get(Calendar.DAY_OF_MONTH))
    }
}