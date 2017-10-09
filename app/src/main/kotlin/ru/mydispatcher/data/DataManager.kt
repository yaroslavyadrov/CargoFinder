package ru.mydispatcher.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.exceptions.Exceptions
import okhttp3.RequestBody
import ru.mydispatcher.data.local.PreferencesHelper
import ru.mydispatcher.data.local.PreferencesHelperImpl
import ru.mydispatcher.data.model.*
import ru.mydispatcher.data.remote.Api
import ru.mydispatcher.data.remote.postparams.CheckCodeBody
import ru.mydispatcher.data.remote.postparams.GuestTokenBody
import ru.mydispatcher.data.remote.postparams.SendCodeBody
import ru.mydispatcher.util.UserType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(
        private val api: Api,
        private val prefs: PreferencesHelperImpl
) : PreferencesHelper by prefs {

    private inline fun <R> makeRequest(request: Api.() -> Single<BaseResponse<R>>): Single<BaseResponse<R>> {
        return api.request()
                .doOnSuccess { (code, message) ->
                    when {
                        code != 0 -> throw Exceptions.propagate(CargoFinderException(message))
                    }
                }
    }

    fun getGuestToken(deviceId: String): Completable {
        val body = GuestTokenBody(deviceId)
        return makeRequest { api.getGuestToken(body) }
                .doOnSuccess {
                    token = it.data.token
                    authorized = false
                }
                .toCompletable()
    }

    fun sendCode(countryCode: String, phone: String): Completable {
        val body = SendCodeBody(countryCode, phone)
        return makeRequest { api.sendCode(body) }
                .toCompletable()
    }

    fun checkCode(code: String): Single<UserType> {
        val body = CheckCodeBody(code)
        return makeRequest { checkCode(body) }
                .doOnSuccess {
                    token = it.data.token
                    authorized = true
                }
                .map { result ->
                    if (result.data.userType == UserType.CUSTOMER.type) {
                        UserType.CUSTOMER
                    } else {
                        UserType.DRIVER
                    }
                }
    }

    fun getGeoObjects(
            query: String,
            geoObjectType: String,
            offset: Int
    ): Observable<List<GeoObject>> {
        return makeRequest {
            api.findCities(
                    query = query,
                    geoObjectType = geoObjectType,
                    offset = offset)
        }.toObservable()
                .map { it.data.list }
    }

    fun registerCustomer(params: Map<String, RequestBody>): Completable {
        return makeRequest { api.registerCustomer(params) }.toCompletable()
    }

    fun getCustomerOrders(
            type: String,
            offset: Int
    ): Observable<List<CustomerOrder>> {
        return makeRequest {
            api.getCustomerOrders(type, offset = offset)
        }.toObservable().map { it.data.orders }
    }

}