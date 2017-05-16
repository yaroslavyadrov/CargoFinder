package com.example.yaroslavyadrov.cargofinder.data

import com.example.yaroslavyadrov.cargofinder.data.local.PreferencesHelper
import com.example.yaroslavyadrov.cargofinder.data.model.Joke
import com.example.yaroslavyadrov.cargofinder.data.remote.Api
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(val api: Api, val prefs: PreferencesHelper) {
    fun getRandomJokes(count: Int): Observable<List<Joke>> {
        prefs.hashCode()
        return api
                .fetchRandomJokes(count)
                .map { it.value }
    }
}