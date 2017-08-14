package ru.mydispatcher.data.local

import org.joda.time.DateTime

interface PreferencesHelper {
    var token: String
    var authorized: Boolean
    var previousSmsTime: DateTime
}