package com.mjperezm.v3_fitlife.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mjperezm.v3_fitlife.data.local.SessionManager
import com.mjperezm.v3_fitlife.data.remote.ApiService
import com.mjperezm.v3_fitlife.data.remote.RetrofitClient
import com.mjperezm.v3_fitlife.data.remote.dto.LoginRequest
import com.mjperezm.v3_fitlife.data.remote.dto.SignupRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val apiService: ApiService = RetrofitClient
        .create(application)
        .create(ApiService::class.java)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkAuthStatus()
    }

    /**
     * Verifica si hay una sesión activa al iniciar la app
     */
    private fun checkAuthStatus() {
        viewModelScope.launch {
            val token = sessionManager.getAuthToken()
            _uiState.value = _uiState.value.copy(
                isAuthenticated = !token.isNullOrEmpty()
            )
        }
    }

    /**
     * Iniciar sesión
     */
    fun login(email: String, password: String) {
        // Validaciones básicas
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "Por favor completa todos los campos"
            )
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(
                error = "Email inválido"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val request = LoginRequest(email = email, password = password)
                val response = apiService.login(request)

                // Guardar token y userId
                sessionManager.saveAuthToken(response.authToken)
                sessionManager.saveUserId(response.user.id.toString())

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    error = null,
                    successMessage = "¡Bienvenido a FitLife!"
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = when {
                        e.message?.contains("401") == true -> "Email o contraseña incorrectos"
                        e.message?.contains("Unable to resolve host") == true -> "Sin conexión a internet"
                        else -> "Error al iniciar sesión: ${e.localizedMessage}"
                    }
                )
            }
        }
    }

    /**
     * Registrar nuevo usuario
     */
    fun signup(name: String, email: String, password: String, confirmPassword: String) {
        // Validaciones
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "Por favor completa todos los campos"
            )
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(
                error = "Email inválido"
            )
            return
        }

        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(
                error = "La contraseña debe tener al menos 6 caracteres"
            )
            return
        }

        if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(
                error = "Las contraseñas no coinciden"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val request = SignupRequest(
                    name = name,
                    email = email,
                    password = password,
                    role = "user"
                )
                val response = apiService.signup(request)

                // Guardar token y userId
                sessionManager.saveAuthToken(response.authToken)
                sessionManager.saveUserId(response.user.id.toString())

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    successMessage = "¡Cuenta creada exitosamente!"
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = when {
                        e.message?.contains("409") == true -> "Este email ya está registrado"
                        e.message?.contains("Unable to resolve host") == true -> "Sin conexión a internet"
                        else -> "Error al registrar: ${e.localizedMessage}"
                    }
                )
            }
        }
    }

    /**
     * Cerrar sesión
     */
    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _uiState.value = AuthUiState(isAuthenticated = false)
        }
    }

    /**
     * Limpiar errores/mensajes
     */
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            error = null,
            successMessage = null
        )
    }
}