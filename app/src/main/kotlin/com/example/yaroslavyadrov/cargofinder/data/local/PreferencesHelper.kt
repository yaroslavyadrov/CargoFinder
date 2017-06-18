package com.example.yaroslavyadrov.cargofinder.data.local

import android.content.SharedPreferences
import com.example.yaroslavyadrov.cargofinder.util.DATE_TIME_FORMAT
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(val prefs: SharedPreferences) {
    companion object {
        private val KEY_TOKEN = "token"
        private val KEY_AUTHORIZED = "authorized"
        private val KEY_PREVIOUS_SMS_SEND = "previous_sms_time"
    }

    var token: String
        get() = prefs.getString(KEY_TOKEN, "")
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    var authorizad: Boolean
        get() = prefs.getBoolean(KEY_AUTHORIZED, false)
        set(value) = prefs.edit().putBoolean(KEY_AUTHORIZED, value).apply()

    var previousSmsTime: String
        get() = prefs.getString(KEY_PREVIOUS_SMS_SEND, DateTime().withYear(2016).toString(DATE_TIME_FORMAT))
        set(value) = prefs.edit().putString(KEY_PREVIOUS_SMS_SEND, value).apply()
}