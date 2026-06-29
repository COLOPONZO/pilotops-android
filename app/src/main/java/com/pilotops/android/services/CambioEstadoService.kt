package com.pilotops.android.services

import com.pilotops.android.app.Constants
import com.pilotops.android.model.CambioEstado
import com.pilotops.android.network.NetworkSession
import com.pilotops.android.parser.CambioEstadoParser
import okhttp3.Request

/**
 * Servicio responsable de obtener la información de CambioEstado
 * desde Operaciones Portuarias.
 */
class CambioEstadoService {

    fun downloadCambioEstadoHtml(): String {
        val request = Request.Builder()
            .url(Constants.BASE_URL + Constants.CAMBIO_ESTADO_PATH)
            .header("User-Agent", Constants.USER_AGENT)
            .get()
            .build()

        NetworkSession.client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("No se pudo obtener CambioEstado. HTTP ${response.code}")
            }

            return response.body?.string()
                ?: error("CambioEstado devolvió HTML vacío.")
        }
    }

    fun fetchCambioEstado(): List<CambioEstado> {
        val html = downloadCambioEstadoHtml()
        return CambioEstadoParser.parse(html)
    }
}