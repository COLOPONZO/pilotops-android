package com.pilotops.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilotops.android.repository.SessionRepository
import com.pilotops.android.services.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel responsable de coordinar el proceso de autenticación.
 */
class LoginViewModel(
    private val loginService: LoginService = LoginService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        _uiState.value = LoginUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val session = withContext(Dispatchers.IO) {
                    loginService.login(username, password)
                }

                SessionRepository.saveSession(session)

                _uiState.value = LoginUiState(
                    isAuthenticated = true,
                    username = session.username
                )
            } catch (error: Throwable) {
                _uiState.value = LoginUiState(
                    errorMessage = error.message ?: "No se pudo iniciar sesión."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

/**
 * Estado visual de la pantalla de login.
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val username: String? = null,
    val errorMessage: String? = null
)