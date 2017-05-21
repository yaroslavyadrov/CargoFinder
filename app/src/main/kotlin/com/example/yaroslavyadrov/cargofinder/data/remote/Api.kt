package com.example.yaroslavyadrov.cargofinder.data.remote

import com.example.yaroslavyadrov.cargofinder.data.model.BaseResponse
import com.example.yaroslavyadrov.cargofinder.data.model.Token
import com.example.yaroslavyadrov.cargofinder.data.remote.postparams.CheckCodeBody
import com.example.yaroslavyadrov.cargofinder.data.remote.postparams.GuestTokenBody
import com.example.yaroslavyadrov.cargofinder.data.remote.postparams.SendCodeBody
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST


interface Api {
    @POST("/auth/get_guest_token")
    fun getGuestToken(@Body body: GuestTokenBody): Single<BaseResponse<Token>>

    @POST("/auth/send_code")
    fun sendCode(@Body body: SendCodeBody): Single<BaseResponse<Any>>

    @POST("/auth/check_code")
    fun checkCode(@Body body: CheckCodeBody): Single<BaseResponse<Token>>
}
