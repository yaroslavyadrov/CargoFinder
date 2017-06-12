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

    var token: String
        get() = prefs.getString(KEY_TOKEN, "")
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    var authorizad: Boolean
        get() = prefs.getBoolean(KEY_AUTHORIZED, false)
        set(value) = prefs.edit().putBoolean(KEY_AUTHORIZED, value).apply()
}