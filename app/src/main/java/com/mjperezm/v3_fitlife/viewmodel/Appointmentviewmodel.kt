package com.mjperezm.v3_fitlife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mjperezm.v3_fitlife.data.remote.ApiService
import com.mjperezm.v3_fitlife.data.remote.RetrofitClient
import com.mjperezm.v3_fitlife.data.remote.dto.CitaDto
import com.mjperezm.v3_fitlife.data.remote.dto.CreateCitaDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class AppointmentUiState(
    val isLoading: Boolean = false,
    val appointments: List<CitaDto> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null
)

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService = RetrofitClient
        .create(application)
        .create(ApiService::class.java)

    private val _uiState = MutableStateFlow(AppointmentUiState())
    val uiState: StateFlow<AppointmentUiState> = _uiState.asStateFlow()

    init {
        loadAppointments()
    }

    /**
     * Cargar todas las citas del usuario
     */
    fun loadAppointments() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                // Nota: Ajusta el endpoint según tu API
                // Este es un ejemplo, deberás implementar el endpoint en tu ApiService
                val appointments = emptyList<CitaDto>() // apiService.getMyCitas()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    appointments = appointments.sortedBy { it.fecha },
                    error = null
                )
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true ->
                        "Sesión expirada. Por favor inicia sesión nuevamente"
                    e.message?.contains("Unable to resolve host") == true ->
                        "Sin conexión a internet"
                    else -> "Error al cargar citas: ${e.localizedMessage}"
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
            }
        }
    }

    /**
     * Crear nueva cita
     */
    fun bookAppointment(
        entrenadorId: String,
        fecha: String,
        horaInicio: String,
        horaFin: String,
        descripcion: String?
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val createDto = CreateCitaDto(
                    entrenador = entrenadorId,
                    fecha = fecha,
                    horaInicio = horaInicio,
                    horaFin = horaFin,
                    descripcion = descripcion
                )

                // Nota: Implementa este endpoint en tu ApiService
                // val newCita = apiService.createCita(createDto)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Cita agendada exitosamente"
                )

                loadAppointments()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al agendar cita: ${e.localizedMessage}"
                )
            }
        }
    }

    /**
     * Cancelar una cita
     */
    fun cancelAppointment(citaId: String) {
        viewModelScope.launch {
            try {
                // Implementa el endpoint de cancelación
                // apiService.cancelCita(citaId)

                _uiState.value = _uiState.value.copy(
                    successMessage = "Cita cancelada"
                )

                loadAppointments()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cancelar cita: ${e.localizedMessage}"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            error = null,
            successMessage = null
        )
    }
}

