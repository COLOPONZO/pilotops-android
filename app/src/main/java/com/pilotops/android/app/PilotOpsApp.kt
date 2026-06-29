package com.pilotops.android.app

import androidx.compose.runtime.*
import com.pilotops.android.ui.screens.EstadoScreen
import com.pilotops.android.ui.screens.LoginScreen

enum class AppScreen {
    LOGIN,
    ESTADO
}

@Composable
fun PilotOpsApp() {

    var currentScreen by remember {
        mutableStateOf(AppScreen.LOGIN)
    }

    when (currentScreen) {

        AppScreen.LOGIN -> LoginScreen(
            onLoginSuccess = {
                currentScreen = AppScreen.ESTADO
            }
        )

        AppScreen.ESTADO -> EstadoScreen()
    }
}