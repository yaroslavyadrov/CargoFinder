package com.example.yaroslavyadrov.cargofinder.data

import com.example.yaroslavyadrov.cargofinder.data.local.PreferencesHelper
import com.example.yaroslavyadrov.cargofinder.data.model.BaseResponse
import com.example.yaroslavyadrov.cargofinder.data.model.CargoFinderException
import com.example.yaroslavyadrov.cargofinder.data.remote.Api
import com.example.yaroslavyadrov.cargofinder.data.remote.postparams.CheckCodeBody
import com.example.yaroslavyadrov.cargofinder.data.remote.postparams.GuestTokenBody
import com.example.yaroslavyadrov.cargofinder.data.remote.postparams.SendCodeBody
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.exceptions.Exceptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(val api: Api, val prefs: PreferencesHelper) {

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
                    prefs.token = it.data.token
                    prefs.authorizad = false
                }
                .toCompletable()
    }

    fun sendCode(countryCode: String, phone: String): Completable {
        val body = SendCodeBody(countryCode, phone)
        return makeRequest { api.sendCode(body) }
                .toCompletable()
    }

    fun checkCode(code: String): Completable {
        val body = CheckCodeBody(code)
        return makeRequest { checkCode(body) }
                .doOnSuccess {
                    prefs.token = it.data.token
                    prefs.authorizad = true
                }
                .toCompletable()
    }

}