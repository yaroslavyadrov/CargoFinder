package ru.mydispatcher.data.local

import android.content.SharedPreferences
import org.joda.time.DateTime
import ru.mydispatcher.util.DATE_TIME_FORMAT
import ru.mydispatcher.util.DEFAULT_DATE_FORMATTER
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelperImpl @Inject constructor(val prefs: SharedPreferences) : PreferencesHelper {
    companion object {
        private val KEY_TOKEN = "token"
        private val KEY_AUTHORIZED = "authorized"
        private val KEY_PREVIOUS_SMS_SEND = "previous_sms_time"
    }

    override var token: String
        get() = prefs.getString(KEY_TOKEN, "")
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    override var authorized: Boolean
        get() = prefs.getBoolean(KEY_AUTHORIZED, false)
        set(value) = prefs.edit().putBoolean(KEY_AUTHORIZED, value).apply()

    override var previousSmsTime: DateTime
        get() {
            val time = prefs.getString(KEY_PREVIOUS_SMS_SEND, DateTime().withYear(2016).toString(DATE_TIME_FORMAT))
            return DEFAULT_DATE_FORMATTER.parseDateTime(time)
        }
        set(value) = prefs.edit().putString(KEY_PREVIOUS_SMS_SEND, value.toString(DEFAULT_DATE_FORMATTER)).apply()
}