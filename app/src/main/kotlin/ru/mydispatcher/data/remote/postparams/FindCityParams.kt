package ru.mydispatcher.data.remote.postparams

import com.google.gson.annotations.SerializedName

data class FindCityParams(
        val query: String,
        @SerializedName("country_id") val countryId: String,
        @SerializedName("geo_object_type") val geoObjectType: String, //valid values "cities" "regions"
        val limit: Int = 30,
        val offset: Int = 0
)
