package ru.mydispatcher.data.model

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime


data class CustomerOrder(
        val id: Int,
        val views: Int,
        @SerializedName("drivers_count_total") val driversCountTotal: Int,
        @SerializedName("drivers_count_new") val driversCountNew: Int,
        @SerializedName("cargo_name") val cargoName: String,
        @SerializedName("cargo_weight") val cargoWeight: Int,
        @SerializedName("weight_unit") val weightUnit: WeightUnit,
        val price: Int,
        val currency: String,
        @SerializedName("payment_type") val paymentType: PaymentType,
        @SerializedName("start_city") val startCity: GeoObject,
        @SerializedName("finish_city") val finishCity: GeoObject,
        @SerializedName("start_date") val startDate: DateTime,
        @SerializedName("finish_date") val finishDate: DateTime,
        val rating: Int
)

