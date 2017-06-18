package ru.mydispatcher.injection.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import ru.mydispatcher.BuildConfig
import ru.mydispatcher.R
import ru.mydispatcher.data.model.CargoFinderException
import ru.mydispatcher.data.remote.Api
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.exceptions.Exceptions
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
open class NetworkModule constructor(private val app: Application) {

    companion object {
        const val API_URL = "http://gruzovoz.alexkam.ru/"
        const val PREF_FILE_NAME = "cargofinder_preferences"
    }

    @Provides
    @Singleton
    fun providePrefs(): SharedPreferences {
        return app.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideCheckNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            try {
                with(chain) {
                    proceed(request())
                }
            } catch (e: IOException) {
                throw Exceptions.propagate(CargoFinderException(app.getString(R.string.error_no_internet)))
            }
        }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(connectivityInterceptor: Interceptor, prefs: SharedPreferences): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okBuilder.addInterceptor(interceptor)
            okBuilder.connectTimeout(30000, TimeUnit.MILLISECONDS)
            okBuilder.readTimeout(30000, TimeUnit.MILLISECONDS)
        }
        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val token = prefs.getString("token", "")
            val requestBuilder = original.newBuilder()
            if (token.isNotEmpty()) {
                requestBuilder.header("Content-Type", "application/json")
                        .header("X-Auth-Token", token)
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        okBuilder.addInterceptor(authInterceptor)
        okBuilder.addInterceptor(connectivityInterceptor)
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    open fun provideApiService(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}
