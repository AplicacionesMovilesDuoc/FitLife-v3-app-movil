package com.mjperezm.v3_fitlife.data.remote

import com.mjperezm.v3_fitlife.data.remote.dto.*
import retrofit2.http.*

interface ApiService {
    // Autenticación
    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): LoginResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getCurrentUser(): UserDto

    // Planes de entrenamiento (ejemplo)
    @GET("planes")
    suspend fun getPlanes(): List<PlanDto>

    // Progreso del usuario
    @GET("progreso")
    suspend fun getProgreso(): List<ProgresoDto>

    @POST("progreso")
    suspend fun registrarProgreso(@Body progreso: ProgresoDto): ProgresoDto
}

// DTOs adicionales (crear archivos separados)
data class PlanDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val tipo: String // "entrenamiento" o "nutricional"
)

data class ProgresoDto(
    val id: Int?,
    val fecha: String,
    val peso: Double,
    val medidas: String?,
    val notas: String?
)