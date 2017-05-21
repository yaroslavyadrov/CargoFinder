package com.example.yaroslavyadrov.cargofinder.data.local

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(val prefs: SharedPreferences) {
    companion object {
        private val KEY_TOKEN = "token"
        private val KEY_AUTHORIZED = "authorized"
    }

    fun setToken(token: String) = prefs.edit().putString(KEY_TOKEN, token).apply()

    fun isAuthorized(): Boolean = prefs.getBoolean(KEY_AUTHORIZED, false)

    fun setIsAuthorized(isAuthorized: Boolean) {
        prefs.edit().putBoolean(KEY_AUTHORIZED, isAuthorized).apply()
    }

}