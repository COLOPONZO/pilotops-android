package com.pilotops.android.app

/**
 * Constantes globales de PilotOps Android.
 *
 * Este archivo concentra todos los valores fijos utilizados por la aplicación.
 * De esta forma evitamos duplicar información y facilitamos futuras modificaciones.
 */
object Constants {

    /**
     * Nombre de la aplicación.
     */
    const val APP_NAME = "PilotOps"

    /**
     * Empresa utilizada por defecto.
     */
    const val DEFAULT_COMPANY = "Prácticos del Paraná"

    /**
     * URL base de Operaciones Portuarias.
     *
     * Todas las solicitudes HTTP parten de esta dirección.
     */
    const val BASE_URL = "https://operacionesportuarias.com"

    /**
     * Tiempo máximo de espera para establecer conexión.
     */
    const val CONNECT_TIMEOUT_SECONDS = 30L

    /**
     * Tiempo máximo de espera para lectura.
     */
    const val READ_TIMEOUT_SECONDS = 30L

    /**
     * Tiempo máximo de espera para escritura.
     */
    const val WRITE_TIMEOUT_SECONDS = 30L

    /**
     * User-Agent utilizado por todas las conexiones HTTP.
     */
    const val USER_AGENT =
        "Mozilla/5.0 (iPhone; CPU iPhone OS 18_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148"

}