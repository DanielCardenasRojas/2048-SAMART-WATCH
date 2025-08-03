package com.example.a2048.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme

@Composable
fun PantallaDelJuego(
    gestor: GestorDeJuego,
    record: Int,
    alActualizarPuntaje: (Int) -> Unit
) {
    var recargarUI by remember { mutableStateOf(0) }
    var ultimoMovimiento by remember { mutableStateOf(0L) }
    var juegoTerminado by remember { mutableStateOf(false) }

    val solicitadorDeFoco = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        solicitadorDeFoco.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF588273))
            .focusRequester(solicitadorDeFoco)
            .focusable()
            .onKeyEvent { tecla ->
                if (tecla.type == KeyEventType.KeyDown) {
                    val seMovio = when (tecla.key) {
                        Key.DirectionLeft -> gestor.deslizarIzquierda()
                        Key.DirectionRight -> gestor.deslizarDerecha()
                        Key.DirectionUp -> gestor.deslizarArriba()
                        Key.DirectionDown -> gestor.deslizarAbajo()
                        else -> false
                    }
                    if (seMovio) {
                        recargarUI++
                        alActualizarPuntaje(gestor.puntuacion)
                        juegoTerminado = gestor.estaTerminado()
                    }
                    true
                } else false
            }
            .pointerInput(Unit) {
                detectDragGestures { cambio, arrastre ->
                    val (dx, dy) = arrastre
                    val ahora = System.currentTimeMillis()
                    if (ahora - ultimoMovimiento > 150) {
                        val seMovio = when {
                            dx > 50 -> gestor.deslizarDerecha()
                            dx < -50 -> gestor.deslizarIzquierda()
                            dy > 50 -> gestor.deslizarAbajo()
                            dy < -50 -> gestor.deslizarArriba()
                            else -> false
                        }
                        if (seMovio) {
                            recargarUI++
                            alActualizarPuntaje(gestor.puntuacion)
                            ultimoMovimiento = ahora
                            juegoTerminado = gestor.estaTerminado()
                        }
                    }
                    cambio.consume()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        key(recargarUI) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("2048 Reloj", fontSize = 13.sp, color = Color.White)
                Text("Puntos: ${gestor.puntuacion}", color = Color(0xFFF2A44C), fontSize = 11.sp)
                Text("Récord: $record", color = Color(0xFFFB7806), fontSize = 11.sp)
                Spacer(modifier = Modifier.height(4.dp))

                for (i in 0 until gestor.tamaño) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        for (j in 0 until gestor.tamaño) {
                            val valor = gestor.tablero[i][j].valor
                            val colorFicha = obtenerColorFicha(valor)
                            val colorTexto = colorTextoParaFicha(colorFicha)

                            AnimatedVisibility(
                                visible = valor > 0,
                                enter = fadeIn() + scaleIn()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(0.5.dp)
                                        .background(colorFicha, shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = valor.toString(),
                                        fontSize = 10.sp,
                                        color = colorTexto
                                    )
                                }
                            }

                            if (valor == 0) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(0.5.dp)
                                        .background(Color(0xFF030F19), shape = CircleShape) // azul oscuro
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (juegoTerminado) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("¡Juego Terminado!", fontSize = 12.sp, color = Color.Red)
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(onClick = {
                        gestor.reiniciar()
                        recargarUI++
                        juegoTerminado = false
                        alActualizarPuntaje(gestor.puntuacion)
                    }) {
                        Text("Reiniciar", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

fun obtenerColorFicha(valor: Int): Color {
    return when (valor) {
        2    -> Color(0xFFFB7806)
        4    -> Color(0xFFCA4410)
        8    -> Color(0xFF503B33)
        16, 32, 64 -> Color(0xFFF2A44C)
        in 128..2048 -> Color(0xFF030F19)
        else -> Color.DarkGray
    }
}

fun colorTextoParaFicha(color: Color): Color {
    return if (esColorOscuro(color)) Color.White else Color.Black
}

fun esColorOscuro(color: Color): Boolean {
    val oscuridad = 1 - (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
    return oscuridad >= 0.5
}
