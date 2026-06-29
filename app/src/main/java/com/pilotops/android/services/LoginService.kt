package com.pilotops.android.services

import com.pilotops.android.app.Constants
import com.pilotops.android.model.UserSession
import com.pilotops.android.network.NetworkSession
import com.pilotops.android.parser.LoginFormParser
import okhttp3.FormBody
import okhttp3.Request
import java.util.Date

/**
 * Servicio responsable de autenticar al usuario contra Operaciones Portuarias.
 */
class LoginService {

    fun login(username: String, password: String): UserSession {
        val trimmedUser = username.trim()
        val trimmedPass = password.trim()

        require(trimmedUser.isNotEmpty()) { "Usuario vacío." }
        require(trimmedPass.isNotEmpty()) { "Contraseña vacía." }

        NetworkSession.clearCookies()

        val loginHtml = loadLoginPage()
        val formInfo = LoginFormParser.parse(loginHtml)

        postLogin(formInfo, trimmedUser, trimmedPass)

        verifyAuthenticatedSession()

        return UserSession(
            username = trimmedUser,
            cookies = NetworkSession.getCookies(),
            loginDate = Date()
        )
    }

    private fun loadLoginPage(): String {
        val request = Request.Builder()
            .url(Constants.BASE_URL)
            .header("User-Agent", Constants.USER_AGENT)
            .get()
            .build()

        NetworkSession.client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("No se pudo abrir la página de login. HTTP ${response.code}")
            }

            return response.body?.string()
                ?: error("La página de login vino vacía.")
        }
    }

    private fun postLogin(
        formInfo: LoginFormParser.LoginFormInfo,
        username: String,
        password: String
    ) {
        val formBuilder = FormBody.Builder()

        val payloadPairs = formInfo.buildPayload(username, password)
            .split("&")
            .mapNotNull { pair ->
                val parts = pair.split("=", limit = 2)
                if (parts.size == 2) parts[0] to parts[1] else null
            }

        payloadPairs.forEach { (key, value) ->
            formBuilder.addEncoded(key, value)
        }

        val request = Request.Builder()
            .url(formInfo.actionUrl)
            .header("User-Agent", Constants.USER_AGENT)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(formBuilder.build())
            .build()

        NetworkSession.client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("Error del servidor durante login. HTTP ${response.code}")
            }
        }
    }

    private fun verifyAuthenticatedSession() {
        val request = Request.Builder()
            .url(Constants.BASE_URL + Constants.CAMBIO_ESTADO_PATH)
            .header("User-Agent", Constants.USER_AGENT)
            .get()
            .build()

        NetworkSession.client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("No se pudo validar la sesión. HTTP ${response.code}")
            }

            val finalUrl = response.request.url.toString().lowercase()
            val html = response.body?.string()?.lowercase()
                ?: error("La validación devolvió HTML vacío.")

            val definitelyLogin =
                finalUrl.contains("/account/login") &&
                        (
                                html.contains("nombre usuario") ||
                                        html.contains("clave") ||
                                        html.contains("iniciar sesion") ||
                                        html.contains("login")
                                )

            if (definitelyLogin) {
                error("Usuario o contraseña incorrectos.")
            }

            val looksLikeProtectedPage =
                html.contains("en espera") ||
                        html.contains("de franco") ||
                        html.contains("t. proceso") ||
                        html.contains("cambio estado") ||
                        html.contains("volver a pizarra") ||
                        html.contains("practicos")

            if (!looksLikeProtectedPage) {
                error("No se pudo validar la sesión después del login.")
            }
        }
    }
}