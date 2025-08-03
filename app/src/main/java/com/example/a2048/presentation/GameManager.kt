package com.example.a2048.presentation

class GestorDeJuego {
    val tamaño = 4
    val tablero: Array<Array<Ficha>> = Array(tamaño) { Array(tamaño) { Ficha(0) } }
    var puntuacion = 0
        private set

    fun reiniciar() {
        puntuacion = 0
        for (fila in tablero) for (ficha in fila) ficha.valor = 0
        agregarFichaAleatoria()
        agregarFichaAleatoria()
    }

    fun agregarFichaAleatoria() {
        val vacias = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until tamaño) {
            for (j in 0 until tamaño) {
                if (tablero[i][j].valor == 0) vacias.add(i to j)
            }
        }
        if (vacias.isNotEmpty()) {
            val (i, j) = vacias.random()
            tablero[i][j].valor = if ((0..9).random() < 9) 2 else 4
        }
    }

    fun deslizarIzquierda(): Boolean {
        var seMovio = false
        for (i in 0 until tamaño) {
            val fila = tablero[i].map { it.valor }.filter { it != 0 }.toMutableList()
            for (j in 0 until fila.size - 1) {
                if (fila[j] == fila[j + 1]) {
                    fila[j] *= 2
                    puntuacion += fila[j]
                    fila[j + 1] = 0
                    seMovio = true
                }
            }
            val nuevaFila = fila.filter { it != 0 } + List(tamaño - fila.count { it != 0 }) { 0 }
            for (j in 0 until tamaño) {
                if (tablero[i][j].valor != nuevaFila[j]) {
                    tablero[i][j].valor = nuevaFila[j]
                    seMovio = true
                }
            }
        }
        if (seMovio) agregarFichaAleatoria()
        return seMovio
    }

    fun deslizarDerecha(): Boolean {
        var seMovio = false
        for (i in 0 until tamaño) {
            val fila = tablero[i].map { it.valor }.filter { it != 0 }.toMutableList()
            for (j in fila.size - 1 downTo 1) {
                if (fila[j] == fila[j - 1]) {
                    fila[j] *= 2
                    puntuacion += fila[j]
                    fila[j - 1] = 0
                    seMovio = true
                }
            }
            val nuevaFila = List(tamaño - fila.count { it != 0 }) { 0 } + fila.filter { it != 0 }
            for (j in 0 until tamaño) {
                if (tablero[i][j].valor != nuevaFila[j]) {
                    tablero[i][j].valor = nuevaFila[j]
                    seMovio = true
                }
            }
        }
        if (seMovio) agregarFichaAleatoria()
        return seMovio
    }

    fun deslizarArriba(): Boolean {
        var seMovio = false
        for (j in 0 until tamaño) {
            val columna = (0 until tamaño).map { tablero[it][j].valor }.filter { it != 0 }.toMutableList()
            for (i in 0 until columna.size - 1) {
                if (columna[i] == columna[i + 1]) {
                    columna[i] *= 2
                    puntuacion += columna[i]
                    columna[i + 1] = 0
                    seMovio = true
                }
            }
            val nuevaColumna = columna.filter { it != 0 } + List(tamaño - columna.count { it != 0 }) { 0 }
            for (i in 0 until tamaño) {
                if (tablero[i][j].valor != nuevaColumna[i]) {
                    tablero[i][j].valor = nuevaColumna[i]
                    seMovio = true
                }
            }
        }
        if (seMovio) agregarFichaAleatoria()
        return seMovio
    }

    fun deslizarAbajo(): Boolean {
        var seMovio = false
        for (j in 0 until tamaño) {
            val columna = (0 until tamaño).map { tablero[it][j].valor }.filter { it != 0 }.toMutableList()
            for (i in columna.size - 1 downTo 1) {
                if (columna[i] == columna[i - 1]) {
                    columna[i] *= 2
                    puntuacion += columna[i]
                    columna[i - 1] = 0
                    seMovio = true
                }
            }
            val nuevaColumna = List(tamaño - columna.count { it != 0 }) { 0 } + columna.filter { it != 0 }
            for (i in 0 until tamaño) {
                if (tablero[i][j].valor != nuevaColumna[i]) {
                    tablero[i][j].valor = nuevaColumna[i]
                    seMovio = true
                }
            }
        }
        if (seMovio) agregarFichaAleatoria()
        return seMovio
    }

    fun estaTerminado(): Boolean {
        for (i in 0 until tamaño) {
            for (j in 0 until tamaño) {
                if (tablero[i][j].valor == 0) return false
                if (j < tamaño - 1 && tablero[i][j].valor == tablero[i][j + 1].valor) return false
                if (i < tamaño - 1 && tablero[i][j].valor == tablero[i + 1][j].valor) return false
            }
        }
        return true
    }
}
