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
     */
    fun loadUserProfile() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val user = apiService.getCurrentUser()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = user,
                    error = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = when {
                        e.message?.contains("401") == true -> "Sesión expirada. Por favor inicia sesión nuevamente"
                        e.message?.contains("Unable to resolve host") == true -> "Sin conexión a internet"
                        else -> "Error al cargar perfil: ${e.localizedMessage}"
                    }
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