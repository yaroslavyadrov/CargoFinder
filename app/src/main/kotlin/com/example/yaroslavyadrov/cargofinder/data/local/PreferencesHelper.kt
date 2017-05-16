package com.example.yaroslavyadrov.cargofinder.data.local

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(val prefs: SharedPreferences) {
    companion object {
        private val KEY_TOKEN = "token"
    }


}