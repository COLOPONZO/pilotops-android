package com.pilotops.android.repository

import com.pilotops.android.model.UserSession

/**
 * Administra la sesión autenticada actual de PilotOps Android.
 *
 * SessionRepository mantiene en memoria la sesión activa del usuario.
 * Más adelante podrá ampliarse para persistir información segura si fuera necesario.
 */
object SessionRepository {

    private var currentSession: UserSession? = null

    /**
     * Indica si existe una sesión activa.
     */
    val isAuthenticated: Boolean
        get() = currentSession != null

    /**
     * Devuelve la sesión actual, si existe.
     */
    fun getSession(): UserSession? {
        return currentSession
    }

    /**
     * Guarda una nueva sesión autenticada.
     */
    fun saveSession(session: UserSession) {
        currentSession = session
    }

    /**
     * Elimina la sesión actual.
     */
    fun clearSession() {
        currentSession = null
    }
}
