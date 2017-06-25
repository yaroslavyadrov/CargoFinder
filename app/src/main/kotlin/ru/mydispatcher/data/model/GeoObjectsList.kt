package ru.mydispatcher.data.model

import com.google.gson.annotations.SerializedName

data class GeoObjectsList(
        @SerializedName("geo_objects") val list: List<GeoObject>,
        val total: Int
)
