package com.mjperezm.v3_fitlife.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mjperezm.v3_fitlife.data.local.AvatarRepository
import com.mjperezm.v3_fitlife.data.local.SessionManager
import com.mjperezm.v3_fitlife.data.remote.ApiService
import com.mjperezm.v3_fitlife.data.remote.RetrofitClient
import com.mjperezm.v3_fitlife.data.remote.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserDto? = null,
    val avatarUri: Uri? = null,
    val error: String? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val avatarRepository = AvatarRepository(application)
    private val apiService: ApiService = RetrofitClient
        .create(application)
        .create(ApiService::class.java)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
        loadSavedAvatar()
    }

    /**
     * Cargar perfil del usuario desde la API
     * Ahora usa el endpoint /auth/profile que devuelve el usuario completo
     */
    fun loadUserProfile() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                // Intentar obtener el perfil completo con datos adicionales
                val userProfile = try {
                    apiService.getUserProfile()
                    // Si existe perfil de usuario, obtener el user completo
                    apiService.getAuthProfile()
                } catch (e: Exception) {
                    // Si no hay perfil de usuario, solo obtener datos básicos
                    apiService.getAuthProfile()
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = userProfile,
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
                    else -> "Error al cargar perfil: ${e.localizedMessage}"
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
            }
        }
    }

    /**
     * Cargar avatar guardado localmente
     */
    private fun loadSavedAvatar() {
        viewModelScope.launch {
            avatarRepository.getAvatarUri().collect { savedUri ->
                _uiState.update { it.copy(avatarUri = savedUri) }
            }
        }
    }

    /**
     * Actualizar avatar del usuario
     * NOTA: Por ahora solo se guarda localmente.
     * Si quieres subir a un servidor, necesitarás implementar
     * el endpoint de upload en tu API.
     */
    fun updateAvatar(uri: Uri?) {
        viewModelScope.launch {
            avatarRepository.saveAvatarUri(uri)
        }
    }

    /**
     * Limpiar errores
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}