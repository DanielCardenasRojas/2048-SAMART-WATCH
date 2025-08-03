package com.example.a2048.presentation

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import com.example.a2048.presentation.theme._2048Theme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class ActividadPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            AplicacionReloj2048()
        }
    }
}

@Composable
fun AplicacionReloj2048() {
    _2048Theme {
        val contexto = LocalContext.current
        val preferencias = contexto.getSharedPreferences("preferencias_juego", Context.MODE_PRIVATE)

        val estadoRecord = remember { mutableIntStateOf(preferencias.getInt("puntuacion_maxima", 0)) }

        val gestorDeJuego = remember { GestorDeJuego().apply { reiniciar() } }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.TopCenter
        ) {
            PantallaDelJuego(
                gestor = gestorDeJuego,
                record = estadoRecord.intValue
            ) { nuevaPuntuacion ->
                if (nuevaPuntuacion > estadoRecord.intValue) {
                    estadoRecord.intValue = nuevaPuntuacion
                    preferencias.edit().putInt("puntuacion_maxima", nuevaPuntuacion).apply()
                }
            }
        }
    }
}
