package com.example.yaroslavyadrov.cargofinder.ui.main

import android.os.Bundle
import com.example.yaroslavyadrov.cargofinder.R
import com.example.yaroslavyadrov.cargofinder.data.model.Joke
import com.example.yaroslavyadrov.cargofinder.ui.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity(), MainView {

    @Inject lateinit var presenter: MainPresenter

    override fun getLayoutResId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        presenter.bind(this)
        presenter.fetchJokes()
    }


    override fun onFetchJokesSuccess(jokes: List<Joke>) {

    }

    override fun onFetchJokesError(error: Throwable) {

    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

}
