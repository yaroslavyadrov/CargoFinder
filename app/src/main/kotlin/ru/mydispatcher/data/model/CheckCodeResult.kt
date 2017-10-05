package ru.mydispatcher.data.model

import com.google.gson.annotations.SerializedName


data class CheckCodeResult(
        val token: String,
        @SerializedName("user_type") val userType: String
)