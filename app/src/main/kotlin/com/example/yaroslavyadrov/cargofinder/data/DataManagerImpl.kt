package com.example.yaroslavyadrov.cargofinder.data

import com.example.yaroslavyadrov.cargofinder.data.model.Joke
import com.example.yaroslavyadrov.cargofinder.data.remote.Api
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataManagerImpl @Inject constructor(val api: Api) : DataManager {
    override fun getRandomJokes(count: Int): Observable<List<Joke>> {
        return api
                .fetchRandomJokes(count)
                .map { it.value }
    }
}