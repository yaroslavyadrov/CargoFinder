package com.example.yaroslavyadrov.cargofinder.data.remote

import com.example.yaroslavyadrov.cargofinder.data.model.BaseResponse
import com.example.yaroslavyadrov.cargofinder.data.model.Joke
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface Api {
    @GET("/jokes/random/{count}")
    fun fetchRandomJokes(@Path("count") count: Int): Observable<BaseResponse<Joke>>
}
