package com.pilotops.android.model

import java.net.HttpCookie
import java.util.Date

/**
 * Representa una sesión autenticada de un usuario en Operaciones Portuarias.
 *
 * Una instancia de UserSession contiene únicamente la información necesaria
 * para identificar una sesión válida. La persistencia y administración de
 * esta sesión serán responsabilidad de SessionRepository.
 */
data class UserSession(

    /**
     * Nombre de usuario autenticado.
     */
    val username: String,

    /**
     * Cookies devueltas por el servidor luego del login.
     */
    val cookies: List<HttpCookie>,

    /**
     * Fecha y hora en que se inició la sesión.
     */
    val loginDate: Date

)