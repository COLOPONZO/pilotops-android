package com.pilotops.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilotops.android.model.CambioEstado
import com.pilotops.android.network.NetworkSession
import com.pilotops.android.services.CambioEstadoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EstadoViewModel(
    private val cambioEstadoService: CambioEstadoService = CambioEstadoService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EstadoUiState())
    val uiState: StateFlow<EstadoUiState> = _uiState.asStateFlow()

    fun cargarEstado() {
        _uiState.value = EstadoUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val html = cambioEstadoService.downloadCambioEstadoHtml()
                    val estados = cambioEstadoService.fetchCambioEstado()

                    EstadoLoadResult(
                        estados = estados,
                        htmlLength = html.length,
                        cookieCount = NetworkSession.cookieCount(),
                        htmlPreview = html.take(1200)
                    )
                }

                _uiState.value = EstadoUiState(
                    estados = result.estados,
                    htmlLength = result.htmlLength,
                    cookieCount = result.cookieCount,
                    htmlPreview = result.htmlPreview
                )
            } catch (error: Throwable) {
                _uiState.value = EstadoUiState(
                    errorMessage = error.message ?: "No se pudo cargar el estado."
                )
            }
        }
    }
}

data class EstadoUiState(
    val isLoading: Boolean = false,
    val estados: List<CambioEstado> = emptyList(),
    val errorMessage: String? = null,
    val htmlLength: Int = 0,
    val cookieCount: Int = 0,
    val htmlPreview: String = ""
)

private data class EstadoLoadResult(
    val estados: List<CambioEstado>,
    val htmlLength: Int,
    val cookieCount: Int,
    val htmlPreview: String
)
