package ru.mydispatcher.data.remote

import ru.mydispatcher.data.model.BaseResponse
import ru.mydispatcher.data.model.CheckCodeResult
import ru.mydispatcher.data.model.Token
import ru.mydispatcher.data.remote.postparams.CheckCodeBody
import ru.mydispatcher.data.remote.postparams.GuestTokenBody
import ru.mydispatcher.data.remote.postparams.SendCodeBody
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST


interface Api {
    @POST("/auth/get_guest_token")
    fun getGuestToken(@Body body: GuestTokenBody): Single<BaseResponse<Token>>

    @POST("/auth/send_code")
    fun sendCode(@Body body: SendCodeBody): Single<BaseResponse<Nothing>>

    @POST("/auth/check_code")
    fun checkCode(@Body body: CheckCodeBody): Single<BaseResponse<CheckCodeResult>>
}
