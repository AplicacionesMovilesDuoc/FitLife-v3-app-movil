package com.mjperezm.v3_fitlife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mjperezm.v3_fitlife.data.remote.ApiService
import com.mjperezm.v3_fitlife.data.remote.CreateProgresoDto
import com.mjperezm.v3_fitlife.data.remote.ProgresoDto
import com.mjperezm.v3_fitlife.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ProgressUiState(
    val isLoading: Boolean = false,
    val progressList: List<ProgresoDto> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null
)

class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService = RetrofitClient
        .create(application)
        .create(ApiService::class.java)

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadProgress()
    }

    /**
     * Cargar historial de progreso
     */
    fun loadProgress() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val progressList = apiService.getProgreso()

                // Ordenar por fecha más reciente primero
                val sortedList = progressList.sortedByDescending {
                    try {
                        // Intentar parsear la fecha en formato ISO
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                            .parse(it.fecha)?.time ?: 0L
                    } catch (e: Exception) {
                        // Si falla, intentar con formato simple
                        try {
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .parse(it.fecha)?.time ?: 0L
                        } catch (e: Exception) {
                            0L
                        }
                    }
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    progressList = sortedList,
                    error = null
                )

            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true ->
                        "Sesión expirada. Por favor inicia sesión nuevamente"
                    e.message?.contains("Unable to resolve host") == true ->
                        "Sin conexión a internet. Verifica tu red."
                    e.message?.contains("timeout") == true ->
                        "El servidor está tardando en responder. Por favor intenta de nuevo."
                    e.message?.contains("Failed to connect") == true ->
                        "No se puede conectar al servidor. Verifica tu conexión."
                    else -> "Error al cargar progreso: ${e.localizedMessage}"
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
            }
        }
    }

    /**
     * Registrar nuevo progreso
     */
    fun addProgress(peso: Double, medidas: String?, notas: String?) {
        if (peso <= 0) {
            _uiState.value = _uiState.value.copy(
                error = "El peso debe ser mayor a 0"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val progreso = CreateProgresoDto(
                    fecha = dateFormat.format(Date()),
                    peso = peso,
                    medidas = medidas,
                    notas = notas
                )

                apiService.registrarProgreso(progreso)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Progreso registrado exitosamente"
                )

                // Recargar lista
                loadProgress()

            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true ->
                        "Sesión expirada. Por favor inicia sesión nuevamente"
                    e.message?.contains("Unable to resolve host") == true ->
                        "Sin conexión a internet. Verifica tu red."
                    e.message?.contains("timeout") == true ->
                        "El servidor está tardando en responder. Por favor intenta de nuevo."
                    e.message?.contains("Failed to connect") == true ->
                        "No se puede conectar al servidor. Verifica tu conexión."
                    else -> "Error al registrar progreso: ${e.localizedMessage}"
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
            }
        }
    }

    /**
     * Limpiar mensajes
     */
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            error = null,
            successMessage = null
        )
    }
}