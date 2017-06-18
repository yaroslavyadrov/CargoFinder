package ru.mydispatcher.data.remote.postparams

import com.google.gson.annotations.SerializedName


data class GuestTokenBody(
        @SerializedName("device_id") val uid: String
)