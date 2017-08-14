package ru.mydispatcher.data.model

import com.google.gson.annotations.SerializedName


enum class WeightUnit {
    @SerializedName("kilo") KILO,
    @SerializedName("ton") TON
}


enum class PaymentType {
    @SerializedName("cash") CASH,
    @SerializedName("non_cash") NON_CASH,
    @SerializedName("both") BOTH
}