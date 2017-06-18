package com.example.yaroslavyadrov.cargofinder.util

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
val DEFAULT_DATE_FORMATTER: DateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT)
const val EXTRA_PHONE_CODE = "EXTRAS.extra_phone_code"
const val EXTRA_PHONE_NUMBER = "EXTRAS.extra_phone_number"
