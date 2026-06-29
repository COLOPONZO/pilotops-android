package com.pilotops.android.network

import com.pilotops.android.app.Constants
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit

/**
 * Sesión HTTP compartida por toda la aplicación.
 *
 * NetworkSession centraliza la configuración de red de PilotOps Android:
 * timeouts, cookies y cliente HTTP.
 *
 * Todas las comunicaciones con Operaciones Portuarias deberán utilizar
 * esta instancia compartida para conservar una misma sesión autenticada.
 */
object NetworkSession {

    private val cookieManager: CookieManager = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    /**
     * Cliente HTTP principal de la aplicación.
     */
    val client: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(cookieManager))
        .connectTimeout(Constants.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(Constants.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(Constants.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    /**
     * Elimina todas las cookies almacenadas.
     *
     * Se utilizará antes de iniciar una nueva sesión de usuario.
     */
    fun clearCookies() {
        cookieManager.cookieStore.removeAll()
    }

    /**
     * Devuelve la cantidad actual de cookies almacenadas.
     *
     * Es útil para depuración durante el login.
     */
    fun cookieCount(): Int {
        return cookieManager.cookieStore.cookies.size
    }
}