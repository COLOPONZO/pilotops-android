package com.pilotops.android.model

/**
 * Representa una fila de la pantalla CambioEstado de Operaciones Portuarias.
 */
data class CambioEstado(
    val nombre: String,
    val estado: String,
    val fechaPresentacion: String,
    val posicion: Int?,
    val empresa: String?
)