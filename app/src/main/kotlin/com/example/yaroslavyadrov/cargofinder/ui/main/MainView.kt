package com.example.yaroslavyadrov.cargofinder.ui.main

import com.example.yaroslavyadrov.cargofinder.ui.base.MvpView


interface MainView : MvpView {

    fun onFetchSuccess()

    fun onFetchError(error: Throwable)

}