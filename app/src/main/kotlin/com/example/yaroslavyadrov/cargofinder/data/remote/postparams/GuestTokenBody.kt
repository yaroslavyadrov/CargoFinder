package com.example.yaroslavyadrov.cargofinder.data.remote.postparams

import com.google.gson.annotations.SerializedName


data class GuestTokenBody(
        @SerializedName("user_type") val userType: String,
        @SerializedName("device_id") val uid: String
)