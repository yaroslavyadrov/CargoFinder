package ru.mydispatcher.data.model

import com.google.gson.annotations.SerializedName


enum class WeightUnit {
    @SerializedName("kilo") KILO,
    @SerializedName("ton") TON
}

enum class Currency(val type: String) {
    @SerializedName("rub") RUB("rub")
}

enum class PaymentMethod(val method: String) {
    @SerializedName("cash") CASH("cash"),
    @SerializedName("non_cash") NON_CASH("non_cash"),
    @SerializedName("both") BOTH("both")
}