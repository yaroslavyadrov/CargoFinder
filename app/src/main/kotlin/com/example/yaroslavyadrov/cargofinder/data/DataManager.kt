package com.example.yaroslavyadrov.cargofinder.data

import com.example.yaroslavyadrov.cargofinder.data.model.Joke
import io.reactivex.Observable


interface DataManager {

    fun getRandomJokes(count: Int): Observable<List<Joke>>

}