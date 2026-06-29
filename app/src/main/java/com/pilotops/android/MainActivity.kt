package com.pilotops.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pilotops.android.app.PilotOpsApp
import com.pilotops.android.ui.theme.theme.PilotOpsAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PilotOpsAndroidTheme {
                PilotOpsApp()
            }
        }
    }
}