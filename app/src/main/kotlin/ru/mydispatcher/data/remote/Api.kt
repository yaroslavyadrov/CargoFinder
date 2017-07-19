package ru.mydispatcher.data.remote

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.mydispatcher.data.model.BaseResponse
import ru.mydispatcher.data.model.CheckCodeResult
import ru.mydispatcher.data.model.GeoObjectsList
import ru.mydispatcher.data.model.Token
import ru.mydispatcher.data.remote.postparams.CheckCodeBody
import ru.mydispatcher.data.remote.postparams.GuestTokenBody
import ru.mydispatcher.data.remote.postparams.SendCodeBody


interface Api {
    @POST("auth/get_guest_token/")
    fun getGuestToken(@Body body: GuestTokenBody): Single<BaseResponse<Token>>

    @POST("auth/send_code/")
    fun sendCode(@Body body: SendCodeBody): Single<BaseResponse<Nothing>>

    @POST("auth/check_code/")
    fun checkCode(@Body body: CheckCodeBody): Single<BaseResponse<CheckCodeResult>>

    @GET("geocoding/search/")
    fun findCities(@Query("query") query: String,
                   @Query("country_id") countryId: Int = 1, //пока только Россия
                   @Query("geo_object_type") geoObjectType: String = "cities", //valid values "cities" "regions"
                   @Query("limit") limit: Int = 30,
                   @Query("offset") offset: Int = 0): Single<BaseResponse<GeoObjectsList>>
}
