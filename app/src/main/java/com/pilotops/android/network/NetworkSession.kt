package com.pilotops.android.network

import com.pilotops.android.app.Constants
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.HttpCookie
import java.util.concurrent.TimeUnit

/**
 * Sesión HTTP compartida por toda la aplicación.
 */
object NetworkSession {

    private val cookieManager: CookieManager = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    val client: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(cookieManager))
        .connectTimeout(Constants.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(Constants.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(Constants.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    fun clearCookies() {
        cookieManager.cookieStore.removeAll()
    }

    fun getCookies(): List<HttpCookie> {
        return cookieManager.cookieStore.cookies
    }

    fun cookieCount(): Int {
        return cookieManager.cookieStore.cookies.size
    }
}