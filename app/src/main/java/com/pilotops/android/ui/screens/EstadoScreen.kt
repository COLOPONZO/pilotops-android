package com.pilotops.android.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pilotops.android.viewmodel.EstadoViewModel

@Composable
fun EstadoScreen(
    viewModel: EstadoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarEstado()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Cambio de Estado")

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.errorMessage?.let {
            Text("Error: $it")
        }

        Text("Registros: ${uiState.estados.size}")

        LazyColumn {
            items(uiState.estados) { estado ->
                Text(
                    text = "${estado.nombre} | ${estado.estado} | Pos: ${estado.posicion ?: "-"}",
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}