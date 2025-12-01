package com.mjperezm.v3_fitlife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mjperezm.v3_fitlife.data.remote.ApiService
import com.mjperezm.v3_fitlife.data.remote.PlanDto
import com.mjperezm.v3_fitlife.data.remote.PlanNutricionalDto
import com.mjperezm.v3_fitlife.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlanUiState(
    val isLoading: Boolean = false,
    val planesEntrenamiento: List<PlanDto> = emptyList(),
    val planesNutricionales: List<PlanNutricionalDto> = emptyList(),
    val planSeleccionado: PlanDto? = null,
    val planNutricionalSeleccionado: PlanNutricionalDto? = null,
    val error: String? = null
)

class PlanViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService = RetrofitClient
        .create(application)
        .create(ApiService::class.java)

    private val _uiState = MutableStateFlow(PlanUiState())
    val uiState: StateFlow<PlanUiState> = _uiState.asStateFlow()

    init {
        loadPlanesEntrenamiento()
    }

    fun loadPlanesEntrenamiento(tipo: String? = null, dificultad: String? = null) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val planes = apiService.getPlanes(tipo, dificultad)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    planesEntrenamiento = planes,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar planes: ${e.localizedMessage}"
                )
            }
        }
    }

    fun loadPlanesNutricionales(objetivo: String? = null) {
        viewModelScope.launch {
            try {
                val planes = apiService.getPlanesNutricionales(objetivo)
                _uiState.value = _uiState.value.copy(
                    planesNutricionales = planes
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cargar planes nutricionales: ${e.localizedMessage}"
                )
            }
        }
    }

    fun selectPlan(planId: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val plan = apiService.getPlan(planId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    planSeleccionado = plan,
                    error = null
                )

                // Cargar planes nutricionales asociados
                loadPlanesNutricionales()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar plan: ${e.localizedMessage}"
                )
            }
        }
    }

    fun selectPlanNutricional(planId: String) {
        viewModelScope.launch {
            try {
                val plan = apiService.getPlanNutricional(planId)
                _uiState.value = _uiState.value.copy(
                    planNutricionalSeleccionado = plan
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cargar plan nutricional: ${e.localizedMessage}"
                )
            }
        }
    }

    fun searchPlanes(query: String) {
        if (query.isBlank()) {
            loadPlanesEntrenamiento()
            return
        }

        viewModelScope.launch {
            try {
                val planes = apiService.searchPlanes(query)
                _uiState.value = _uiState.value.copy(
                    planesEntrenamiento = planes
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error en búsqueda: ${e.localizedMessage}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            planSeleccionado = null,
            planNutricionalSeleccionado = null
        )
    }
}