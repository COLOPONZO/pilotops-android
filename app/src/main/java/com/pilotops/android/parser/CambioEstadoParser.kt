package com.pilotops.android.parser

import com.pilotops.android.model.CambioEstado
import org.jsoup.Jsoup

/**
 * Parser de la página CambioEstado de Operaciones Portuarias.
 */
object CambioEstadoParser {

    fun parse(html: String): List<CambioEstado> {
        val document = Jsoup.parse(html)
        val rows = document.select("table tbody tr")

        return rows.mapNotNull { row ->
            val columns = row.select("td")

            if (columns.size < 4) {
                return@mapNotNull null
            }

            CambioEstado(
                nombre = columns.getOrNull(0)?.text().orEmpty().trim(),
                estado = columns.getOrNull(1)?.text().orEmpty().trim(),
                fechaPresentacion = columns.getOrNull(2)?.text().orEmpty().trim(),
                posicion = columns.getOrNull(3)?.text()?.trim()?.toIntOrNull(),
                empresa = columns.getOrNull(4)?.text()?.trim()
            )
        }
    }
}