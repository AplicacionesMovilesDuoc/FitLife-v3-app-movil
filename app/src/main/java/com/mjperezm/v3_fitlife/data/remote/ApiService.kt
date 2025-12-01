package com.mjperezm.v3_fitlife.data.remote

import com.mjperezm.v3_fitlife.data.remote.dto.*
import retrofit2.http.*

interface ApiService {
    // ==================== AUTENTICACIÓN ====================
    @POST("auth/register")
    suspend fun signup(@Body request: SignupRequest): LoginResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/profile")
    suspend fun getAuthProfile(): UserDto

    // ==================== PERFIL DE USUARIO ====================
    @GET("usuario-profile/me")
    suspend fun getUserProfile(): UsuarioProfileDto

    @PUT("usuario-profile/me")
    suspend fun updateUserProfile(@Body profile: UpdateUsuarioProfileDto): UsuarioProfileDto

    // ==================== PLANES DE ENTRENAMIENTO ====================
    @GET("plan-entrenamiento")
    suspend fun getPlanes(
        @Query("tipo") tipo: String? = null,
        @Query("dificultad") dificultad: String? = null
    ): List<PlanDto>

    @GET("plan-entrenamiento/{id}")
    suspend fun getPlan(@Path("id") id: String): PlanDto

    @GET("plan-entrenamiento/search")
    suspend fun searchPlanes(@Query("q") query: String): List<PlanDto>

    // ==================== PROGRESO ====================
    @GET("progreso")
    suspend fun getProgreso(): List<ProgresoDto>

    @POST("progreso")
    suspend fun registrarProgreso(@Body progreso: CreateProgresoDto): ProgresoDto

    @GET("progreso/stats")
    suspend fun getProgresoStats(): ProgresoStatsDto

    @GET("progreso/{id}")
    suspend fun getProgresoById(@Path("id") id: String): ProgresoDto

    @PATCH("progreso/{id}")
    suspend fun updateProgreso(
        @Path("id") id: String,
        @Body progreso: UpdateProgresoDto
    ): ProgresoDto

    @DELETE("progreso/{id}")
    suspend fun deleteProgreso(@Path("id") id: String): DeleteResponseDto

    // ==================== PLANES NUTRICIONALES ====================
    @GET("plan-nutricional")
    suspend fun getPlanesNutricionales(
        @Query("objetivo") objetivo: String? = null
    ): List<PlanNutricionalDto>

    @GET("plan-nutricional/{id}")
    suspend fun getPlanNutricional(@Path("id") id: String): PlanNutricionalDto

    @GET("plan-nutricional/search")
    suspend fun searchPlanesNutricionales(@Query("q") query: String): List<PlanNutricionalDto>
}

// ==================== DTOs ADICIONALES ====================

data class UsuarioProfileDto(
    val _id: String,
    val user: String,
    val nombre: String?,
    val telefono: String?,
    val edad: Int?,
    val peso: Double?,
    val altura: Double?,
    val objetivo: String?
)

data class UpdateUsuarioProfileDto(
    val nombre: String?,
    val telefono: String?,
    val edad: Int?,
    val peso: Double?,
    val altura: Double?,
    val objetivo: String?
)

data class PlanDto(
    val _id: String,
    val nombre: String,
    val descripcion: String,
    val duracion: String?,
    val dificultad: String?,
    val tipo: String?,
    val ejercicios: List<EjercicioDto>?,
    val disponible: Boolean?,
    val imagen: String?,
    val imagenThumbnail: String?,
    val createdBy: String?
)

data class EjercicioDto(
    val nombre: String,
    val series: Int,
    val repeticiones: Int,
    val descanso: String,
    val descripcion: String?
)

data class ProgresoDto(
    val _id: String?,
    val userId: String?,
    val fecha: String,
    val peso: Double,
    val medidas: String?,
    val notas: String?
)

data class CreateProgresoDto(
    val fecha: String,
    val peso: Double,
    val medidas: String?,
    val notas: String?
)

data class UpdateProgresoDto(
    val peso: Double?,
    val medidas: String?,
    val notas: String?
)

data class ProgresoStatsDto(
    val totalRegistros: Int,
    val pesoActual: Double?,
    val pesoInicial: Double?,
    val diferencia: Double?,
    val ultimaActualizacion: String?
)

data class DeleteResponseDto(
    val deleted: Boolean,
    val message: String
)

// DTOs

data class PlanNutricionalDto(
    val _id: String,
    val nombre: String,
    val descripcion: String,
    val duracion: String?,
    val objetivo: String?,
    val comidas: List<ComidaDto>?,
    val disponible: Boolean?,
    val imagen: String?,
    val imagenThumbnail: String?,
    val createdBy: String?
)

data class ComidaDto(
    val tipo: String,
    val alimentos: List<String>,
    val calorias: Int,
    val proteinas: Double,
    val carbohidratos: Double,
    val grasas: Double
)

data class CreatePlanNutricionalDto(
    val nombre: String,
    val descripcion: String,
    val duracion: String?,
    val objetivo: String?,
    val comidas: List<ComidaDto>?
)

data class UpdatePlanNutricionalDto(
    val nombre: String?,
    val descripcion: String?,
    val duracion: String?,
    val objetivo: String?,
    val comidas: List<ComidaDto>?
)