package ru.mydispatcher.data.remote

import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*
import ru.mydispatcher.data.model.*
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

    @Multipart
    @POST("customer/registration/")
    fun registerCustomer(@PartMap params: Map<String, @JvmSuppressWildcards RequestBody>): Single<BaseResponse<Nothing>>

    @GET("customer/orders/")
    fun getCustomerOrders(@Query("order_type") orderType: String,//valid values "active" "inactive"
                   @Query("limit") limit: Int = 30,
                   @Query("offset") offset: Int = 0): Single<BaseResponse<CustomerOrdersResponse>>

    @PUT("customer/orders/")
    fun createCustomerOrder(@PartMap body: HashMap<String, String>): Single<BaseResponse<CustomerOrder>>
}
