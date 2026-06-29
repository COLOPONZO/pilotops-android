package com.pilotops.android.parser

import com.pilotops.android.app.Constants
import org.jsoup.Jsoup
import java.net.URLEncoder

/**
 * Analiza el formulario de login de Operaciones Portuarias
 * y extrae toda la información necesaria para realizar
 * posteriormente el POST de autenticación.
 */
object LoginFormParser {

    data class LoginFormInfo(
        val actionUrl: String,
        val hiddenFields: MutableMap<String, String>,
        val userField: String,
        val passwordField: String,
        val submitField: String?,
        val submitValue: String?
    ) {

        fun buildPayload(
            username: String,
            password: String
        ): String {

            val params = hiddenFields.toMutableMap()

            params[userField] = username
            params[passwordField] = password

            if (submitField != null) {
                params[submitField] = submitValue ?: "Ingresar"
            }

            return params.entries.joinToString("&") {

                val key = URLEncoder.encode(it.key, "UTF-8")
                val value = URLEncoder.encode(it.value, "UTF-8")

                "$key=$value"
            }
        }
    }

    fun parse(html: String): LoginFormInfo {

        val document = Jsoup.parse(html)

        val form = document.selectFirst("form")
            ?: error("No se encontró el formulario de login.")

        val action = form.attr("action")

        val actionUrl =
            if (action.startsWith("http")) {
                action
            } else if (action.startsWith("/")) {
                Constants.BASE_URL + action
            } else {
                Constants.BASE_URL + "/" + action
            }

        val hiddenFields = mutableMapOf<String, String>()

        form.select("input[type=hidden]").forEach {

            val name = it.attr("name")
            val value = it.attr("value")

            if (name.isNotBlank()) {
                hiddenFields[name] = value
            }
        }

        var userField = ""
        var passwordField = ""
        var submitField: String? = null
        var submitValue: String? = null

        form.select("input").forEach {

            val type = it.attr("type").lowercase()
            val name = it.attr("name")

            when {

                type == "text" && userField.isEmpty() ->
                    userField = name

                type == "email" && userField.isEmpty() ->
                    userField = name

                type == "password" ->
                    passwordField = name

                type == "submit" -> {
                    submitField = name
                    submitValue = it.attr("value")
                }
            }
        }

        require(userField.isNotBlank()) {
            "Campo usuario no encontrado."
        }

        require(passwordField.isNotBlank()) {
            "Campo contraseña no encontrado."
        }

        return LoginFormInfo(
            actionUrl = actionUrl,
            hiddenFields = hiddenFields,
            userField = userField,
            passwordField = passwordField,
            submitField = submitField,
            submitValue = submitValue
        )
    }
}