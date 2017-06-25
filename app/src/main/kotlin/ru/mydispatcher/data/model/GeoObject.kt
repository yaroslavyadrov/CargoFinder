package ru.mydispatcher.data.model

import com.google.gson.annotations.SerializedName

data class GeoObject(
        val id: Int,
        val name: String,
        @SerializedName("parent_name") val parentName: String,
        @SerializedName("parent_id") val parentId: Int,
        val region: Boolean,
        @SerializedName("country_name") val countryName: String,
        @SerializedName("country_id") val countryId: String
)
