package com.example.yaroslavyadrov.cargofinder.ui.main

import com.example.yaroslavyadrov.cargofinder.data.model.Joke
import com.example.yaroslavyadrov.cargofinder.ui.base.MvpView


interface MainView : MvpView {

    fun onFetchJokesSuccess(jokes: List<Joke>)

    fun onFetchJokesError(error: Throwable)

}