package com.example.yaroslavyadrov.cargofinder.ui.main

import com.example.yaroslavyadrov.cargofinder.data.DataManager
import com.example.yaroslavyadrov.cargofinder.data.model.Joke
import com.example.yaroslavyadrov.cargofinder.injection.scope.PerActivity
import com.example.yaroslavyadrov.cargofinder.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

@PerActivity
class MainPresenter @Inject constructor(val dataManager: DataManager) : BasePresenter<MainView>() {

    fun fetchJokes() {
        disposables.add(
                dataManager.getRandomJokes(100)
                        .subscribeOn(io())
                        .observeOn(mainThread())
                        .subscribe(
                                { onFetchJokesSuccess(it) },
                                { onFetchJokesError(it) })
        )
    }

    fun onFetchJokesSuccess(jokes: List<Joke>) {
        view?.onFetchJokesSuccess(jokes)
    }

    fun onFetchJokesError(error: Throwable) {
        view?.onFetchJokesError(error)
    }

}