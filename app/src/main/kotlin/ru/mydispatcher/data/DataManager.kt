package ru.mydispatcher.data

import ru.mydispatcher.data.local.PreferencesHelper
import ru.mydispatcher.data.model.BaseResponse
import ru.mydispatcher.data.model.CargoFinderException
import ru.mydispatcher.data.remote.Api
import ru.mydispatcher.data.remote.postparams.CheckCodeBody
import ru.mydispatcher.data.remote.postparams.GuestTokenBody
import ru.mydispatcher.data.remote.postparams.SendCodeBody
import ru.mydispatcher.util.DATE_TIME_FORMAT
import ru.mydispatcher.util.DEFAULT_DATE_FORMATTER
import ru.mydispatcher.util.UserType
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.exceptions.Exceptions
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(private val api: Api, private val prefs: PreferencesHelper) {

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

    fun checkCode(code: String): Single<UserType> {
        val body = CheckCodeBody(code)
        return makeRequest { checkCode(body) }
                .doOnSuccess {
                    prefs.token = it.data.token
                    prefs.authorizad = true
                }
                .map { result ->
                    if (result.data.userType == UserType.CUSTOMER.type) {
                        UserType.CUSTOMER
                    } else {
                        UserType.DRIVER
                    }
                }
    }

    fun getPreviousDateOfSms(): DateTime = DEFAULT_DATE_FORMATTER.parseDateTime(prefs.previousSmsTime)

    fun setPreviousDateOfSms(date: DateTime) {
        prefs.previousSmsTime = date.toString(DATE_TIME_FORMAT)
    }

}