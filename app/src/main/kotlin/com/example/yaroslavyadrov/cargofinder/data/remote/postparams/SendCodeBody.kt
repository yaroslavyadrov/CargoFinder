package com.example.yaroslavyadrov.cargofinder.data.remote.postparams

import com.google.gson.annotations.SerializedName


data class SendCodeBody(
        @SerializedName("country_code") val countryCode: String,
        @SerializedName("phone") val phone: String
)
