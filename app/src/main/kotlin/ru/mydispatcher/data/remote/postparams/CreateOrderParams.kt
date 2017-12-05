package ru.mydispatcher.data.remote.postparams

import ru.mydispatcher.data.model.Currency
import ru.mydispatcher.data.model.PaymentMethod

data class CreateOrderParams(
        private val cargoName: String,
        private val cargoWeight: Int,
        private val price: Int = 0,
        private val currency: Currency = Currency.RUB,
        private val paymentMethod: PaymentMethod,
        private val startCityId: Int,
        private val finishCityId: Int = 0,
        private val startDate: String = "",
        private val finishDate: String = "",
        private val comment: String = "",
        private val bodyTypesIds: List<String> = emptyList()
) {
    val map: HashMap<String, String>
        get() {
            val params = HashMap<String, String>()
            params["cargo_name"] = cargoName
            params["cargo_weight"] = cargoWeight.toString()
            if (price != 0) params["price"] = price.toString()
            params["currency"] = currency.type
            params["payment_method"] = paymentMethod.method
            params["start_city_id"] = startCityId.toString()
            if (finishCityId != 0) params["finish_city_id"] = finishCityId.toString()
            if (startDate.isNotEmpty()) params["start_date"] = startDate
            if (finishDate.isNotEmpty()) params["finish_date"] = finishDate
            if (comment.isNotEmpty()) params["comment"] = comment
            if (bodyTypesIds.isNotEmpty()) params["body_types_ids"] = bodyTypesIds.toString()
            return params
        }
}



