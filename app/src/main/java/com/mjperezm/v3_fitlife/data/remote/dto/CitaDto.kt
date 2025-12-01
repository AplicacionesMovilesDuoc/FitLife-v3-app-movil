package com.mjperezm.v3_fitlife.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para representar una cita/reserva
 */
data class CitaDto(
    @SerializedName("_id")
    val id: String?,

    @SerializedName("usuario")
    val usuario: String, // ID del usuario

    @SerializedName("usuarioNombre")
    val usuarioNombre: String?, // Nombre del usuario (populate)

    @SerializedName("entrenador")
    val entrenador: String, // ID del entrenador

    @SerializedName("entrenadorNombre")
    val entrenadorNombre: String?, // Nombre del entrenador (populate)

    @SerializedName("fecha")
    val fecha: String, // Formato: YYYY-MM-DD

    @SerializedName("horaInicio")
    val horaInicio: String, // Formato: HH:mm (24h)

    @SerializedName("horaFin")
    val horaFin: String, // Formato: HH:mm (24h)

    @SerializedName("sucursal")
    val sucursal: String? = "Sucursal Principal",

    @SerializedName("estado")
    val estado: String = "CONFIRMADA", // CONFIRMADA, CANCELADA, COMPLETADA

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null
)

/**
 * DTO para crear una nueva cita
 */
data class CreateCitaDto(
    @SerializedName("entrenador")
    val entrenador: String,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("horaInicio")
    val horaInicio: String,

    @SerializedName("horaFin")
    val horaFin: String,

    @SerializedName("descripcion")
    val descripcion: String? = null
)

/**
 * DTO para obtener disponibilidad de horarios
 */
data class DisponibilidadDto(
    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("horariosDisponibles")
    val horariosDisponibles: List<HorarioDto>
)

data class HorarioDto(
    @SerializedName("horaInicio")
    val horaInicio: String,

    @SerializedName("horaFin")
    val horaFin: String,

    @SerializedName("entrenadoresDisponibles")
    val entrenadoresDisponibles: List<EntrenadorDisponibleDto>? = null
)

data class EntrenadorDisponibleDto(
    @SerializedName("_id")
    val id: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("especialidad")
    val especialidad: String? = null
)

/**
 * DTO para planes de entrenamiento y nutrición asignados
 */
data class PlanAsignadoDto(
    @SerializedName("_id")
    val id: String?,

    @SerializedName("usuario")
    val usuario: String,

    @SerializedName("usuarioNombre")
    val usuarioNombre: String?,

    @SerializedName("entrenador")
    val entrenador: String,

    @SerializedName("entrenadorNombre")
    val entrenadorNombre: String?,

    @SerializedName("planEntrenamiento")
    val planEntrenamiento: String?,

    @SerializedName("planNutricional")
    val planNutricional: String?,

    @SerializedName("createdAt")
    val createdAt: String?,

    @SerializedName("updatedAt")
    val updatedAt: String?
)

/**
 * DTO para crear/actualizar plan asignado
 */
data class UpdatePlanAsignadoDto(
    @SerializedName("planEntrenamiento")
    val planEntrenamiento: String?,

    @SerializedName("planNutricional")
    val planNutricional: String?
)