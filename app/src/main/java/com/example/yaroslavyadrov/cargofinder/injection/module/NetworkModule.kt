package com.example.yaroslavyadrov.cargofinder.injection.module

import android.app.Application
import com.example.yaroslavyadrov.cargofinder.BuildConfig
import com.example.yaroslavyadrov.cargofinder.data.remote.Api
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class NetworkModule constructor(private val app: Application) {

    companion object {
        const val API_URL = "http://api.icndb.com/"
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okBuilder.addInterceptor(interceptor)
            okBuilder.connectTimeout(30000, TimeUnit.MILLISECONDS)
            okBuilder.readTimeout(30000, TimeUnit.MILLISECONDS)
        }
        okBuilder.addInterceptor(ChuckInterceptor(app))
        return okBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    open fun provideJokesService(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}
